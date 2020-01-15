package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.dtos.ValidateTokenDto;
import com.tericcabrel.authorization.events.OnRegistrationCompleteEvent;
import com.tericcabrel.authorization.models.ConfirmAccount;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.models.redis.RefreshToken;
import com.tericcabrel.authorization.repositories.RefreshTokenRepository;
import com.tericcabrel.authorization.services.interfaces.ConfirmAccountService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.tericcabrel.authorization.dtos.LoginUserDto;
import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.Role;
import com.tericcabrel.authorization.models.common.ApiResponse;
import com.tericcabrel.authorization.models.common.AuthToken;
import com.tericcabrel.authorization.services.interfaces.RoleService;
import com.tericcabrel.authorization.services.interfaces.UserService;
import com.tericcabrel.authorization.utils.JwtTokenUtil;
import com.tericcabrel.authorization.utils.Helpers;

import static com.tericcabrel.authorization.utils.Constants.ROLE_USER;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    private RoleService roleService;

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private RefreshTokenRepository refreshTokenRepository;

    private ApplicationEventPublisher eventPublisher;

    private ConfirmAccountService confirmAccountService;

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtTokenUtil jwtTokenUtil,
        UserService userService,
        RoleService roleService,
        RefreshTokenRepository refreshTokenRepository,
        ApplicationEventPublisher eventPublisher,
        ConfirmAccountService confirmAccountService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.roleService = roleService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.eventPublisher = eventPublisher;
        this.confirmAccountService = confirmAccountService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserDto userDto) {
        Role role = roleService.findByName(ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userDto.setRoles(roles);

        // User user = userService.save(userDto);
        User u = userService.findByEmail("tericcabrel@yahoo.com");

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(u));

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), null));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginUserDto loginUserDto) throws AuthenticationException {
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

            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        if (!user.isConfirmed()) {
            result.put("data", "Your account isn't confirmed yet!");

            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.createTokenFromAuth(authentication);

        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        String refreshToken = Helpers.generateRandomString(25);

        RefreshToken refreshTokenObject = new RefreshToken(user.getId(), refreshToken);
        refreshTokenRepository.save(refreshTokenObject);

        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), new AuthToken(token, refreshToken, expirationDate.getTime()))
        );
    }

    @PostMapping(value = "/confirm-account")
    public ResponseEntity<ApiResponse> confirmAccount(@Valid @RequestBody ValidateTokenDto validateTokenDto) {
        ConfirmAccount confirmAccount = confirmAccountService.findByToken(validateTokenDto.getToken());
        HashMap<String, String> result = new HashMap<>();

        if (confirmAccount == null) {
            result.put("message", "The token is invalid!");

            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        if (confirmAccount.getExpireAt() < new Date().getTime()) {
            result.put("message", "You token has been expired!");

            confirmAccountService.delete(confirmAccount.getId());

            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        User user = userService.findById(confirmAccount.getUser().getId());

        user.setConfirmed(true);
        userService.update(user);

        result.put("message", "Your account confirmed successfully!");

        return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.OK.value(), result));
    }
}
