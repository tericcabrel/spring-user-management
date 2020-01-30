package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;
import java.util.*;

import static com.tericcabrel.authorization.utils.Constants.ROLE_USER;

import com.tericcabrel.authorization.dtos.LoginUserDto;
import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.dtos.ValidateTokenDto;
import com.tericcabrel.authorization.models.common.*;
import com.tericcabrel.authorization.models.Role;
import com.tericcabrel.authorization.models.ConfirmAccount;
import com.tericcabrel.authorization.models.User;
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

    private IUserService userService;

    private IRoleService roleService;

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private RefreshTokenRepository refreshTokenRepository;

    private ApplicationEventPublisher eventPublisher;

    private IConfirmAccountService confirmAccountService;

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

    @ApiOperation(value = "Register a new user in the system", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registered successfully!", response = UserResponse.class),
        @ApiResponse(code = 422, message = "One or many parameters in the request's body are invalid", response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/register")
    public ResponseEntity<ServiceResponse> register(@Valid @RequestBody UserDto userDto) {
        Role role = roleService.findByName(ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userDto.setRoles(roles);

        User user = userService.save(userDto);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));

        return ResponseEntity.ok(new ServiceResponse(HttpStatus.OK.value(), user));
    }

    @ApiOperation(value = "Authenticate an user", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Authenticated successfully!", response = AuthTokenResponse.class),
        @ApiResponse(code = 400, message = "Bad credentials | The account is deactivated | The account isn't confirmed yet", response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = "You don't have the right to access to this resource", response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = "One or many parameters in the request's body are invalid", response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/login")
    public ResponseEntity<ServiceResponse> login(@Valid @RequestBody LoginUserDto loginUserDto) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginUserDto.getEmail(),
                    loginUserDto.getPassword()
                )
        );

        User user = userService.findByEmail(loginUserDto.getEmail());
        HashMap<String, String> result = new HashMap<>();

        if (!user.isEnabled()) {
            result.put("data", "Your account has been deactivated!");

            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        if (!user.isConfirmed()) {
            result.put("data", "Your account isn't confirmed yet!");

            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.createTokenFromAuth(authentication);

        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        String refreshToken = Helpers.generateRandomString(25);

        RefreshToken refreshTokenObject = new RefreshToken(user.getId(), refreshToken);
        refreshTokenRepository.save(refreshTokenObject);

        return ResponseEntity.ok(
            new ServiceResponse(HttpStatus.OK.value(), new AuthTokenResponse(token, refreshToken, expirationDate.getTime()))
        );
    }

    @ApiOperation(value = "Confirm the account of an user", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Account confirmed successfully!", response = SuccessResponse.class),
        @ApiResponse(code = 400, message = "The token is invalid | The token has been expired", response = BadRequestResponse.class),
    })
    @PostMapping(value = "/confirm-account")
    public ResponseEntity<ServiceResponse> confirmAccount(@Valid @RequestBody ValidateTokenDto validateTokenDto) {
        ConfirmAccount confirmAccount = confirmAccountService.findByToken(validateTokenDto.getToken());
        HashMap<String, String> result = new HashMap<>();

        if (confirmAccount == null) {
            result.put("message", "The token is invalid!");

            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        if (confirmAccount.getExpireAt() < new Date().getTime()) {
            result.put("message", "You token has been expired!");

            confirmAccountService.delete(confirmAccount.getId());

            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        User user = userService.findById(confirmAccount.getUser().getId());

        user.setConfirmed(true);
        userService.update(user);

        result.put("message", "Your account confirmed successfully!");

        return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.OK.value(), result));
    }
}
