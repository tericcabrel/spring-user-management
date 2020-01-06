package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.models.redis.RefreshToken;
import com.tericcabrel.authorization.repositories.RefreshTokenRepository;
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

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtTokenUtil jwtTokenUtil,
        UserService userService,
        RoleService roleService,
        RefreshTokenRepository refreshTokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.roleService = roleService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserDto userDto) {
        Role role = roleService.findById(ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userDto.setRoles(roles);

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), userService.save(userDto)));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody LoginUserDto loginUserDto) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginUserDto.getEmail(),
                    loginUserDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.createToken(authentication);

        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        String refreshToken = Helpers.generateRandomString(25);

        RefreshToken refreshTokenObject = new RefreshToken("user_id", refreshToken);
        refreshTokenRepository.save(refreshTokenObject);

        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), new AuthToken(token, refreshToken, expirationDate.getTime()))
        );
    }
}
