package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.tericcabrel.authorization.utils.Constants.*;

import com.tericcabrel.authorization.models.dto.LoginUserDto;
import com.tericcabrel.authorization.models.dto.CreateUserDto;
import com.tericcabrel.authorization.models.dto.ValidateTokenDto;
import com.tericcabrel.authorization.models.response.*;
import com.tericcabrel.authorization.models.mongo.ConfirmAccount;
import com.tericcabrel.authorization.models.mongo.User;
import com.tericcabrel.authorization.models.redis.RefreshToken;
import com.tericcabrel.authorization.repositories.redis.RefreshTokenRepository;
import com.tericcabrel.authorization.services.interfaces.IRoleService;
import com.tericcabrel.authorization.services.interfaces.IUserService;
import com.tericcabrel.authorization.services.interfaces.IConfirmAccountService;
import com.tericcabrel.authorization.utils.JwtTokenUtil;
import com.tericcabrel.authorization.utils.Helpers;
import com.tericcabrel.authorization.events.OnRegistrationCompleteEvent;


@Api(tags = "Authorization management", description = "Operations pertaining to registration, authentication and account confirmation")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IUserService userService;

    private final IRoleService roleService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final IConfirmAccountService confirmAccountService;

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtTokenUtil jwtTokenUtil,
        IUserService userService,
        IRoleService roleService,
        RefreshTokenRepository refreshTokenRepository,
        ApplicationEventPublisher eventPublisher,
        IConfirmAccountService confirmAccountService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.roleService = roleService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.eventPublisher = eventPublisher;
        this.confirmAccountService = confirmAccountService;
    }

    @ApiOperation(value = "Register a new user in the system", response = BadRequestResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Registered successfully!", response = UserResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/register")
    public ResponseEntity<User> register(@Valid @RequestBody CreateUserDto createUserDto) {
        createUserDto.setRoles(
            new HashSet<>(
                Collections.singletonList(roleService.findByName(ROLE_USER))
            )
        );

        User user = userService.save(createUserDto);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));

        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Authenticate an user", response = BadRequestResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Authenticated successfully!", response = AuthTokenResponse.class),
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad credentials | The account is deactivated | The account isn't confirmed yet", response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginUserDto.getEmail(),
                    loginUserDto.getPassword()
                )
        );

        User user = userService.findByEmail(loginUserDto.getEmail());
        Map<String, String> result = new HashMap<>();

        if (!user.isEnabled()) {
            result.put(DATA_KEY, "Your account has been deactivated!");

            return ResponseEntity.badRequest().body(result);
        }

        if (!user.isConfirmed()) {
            result.put(DATA_KEY, "Your account isn't confirmed yet!");

            return ResponseEntity.badRequest().body(result);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.createTokenFromAuth(authentication);

        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        String refreshToken = Helpers.generateRandomString(25);

        RefreshToken refreshTokenObject = new RefreshToken(user.getId(), refreshToken);
        refreshTokenRepository.save(refreshTokenObject);

        return ResponseEntity.ok(new AuthTokenResponse(token, refreshToken, expirationDate.getTime()));
    }

    @ApiOperation(value = "Confirm the account of an user", response = SuccessResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Account confirmed successfully!", response = SuccessResponse.class),
        @io.swagger.annotations.ApiResponse(code = 400, message = "The token is invalid | The token has been expired", response = BadRequestResponse.class),
    })
    @PostMapping(value = "/confirm-account")
    public ResponseEntity<Object> confirmAccount(@Valid @RequestBody ValidateTokenDto validateTokenDto) {
        ConfirmAccount confirmAccount = confirmAccountService.findByToken(validateTokenDto.getToken());
        Map<String, String> result = new HashMap<>();

        if (confirmAccount == null) {
            result.put(MESSAGE_KEY, INVALID_TOKEN_MESSAGE);

            return ResponseEntity.badRequest().body(result);
        }

        if (confirmAccount.getExpireAt() < new Date().getTime()) {
            result.put(MESSAGE_KEY, TOKEN_EXPIRED_MESSAGE);

            confirmAccountService.delete(confirmAccount.getId());

            return ResponseEntity.badRequest().body(result);
        }

        User user = userService.findById(confirmAccount.getUser().getId());

        user.setConfirmed(true);
        userService.update(user);

        result.put(MESSAGE_KEY, "Your account confirmed successfully!");

        return ResponseEntity.badRequest().body(result);
    }
}
