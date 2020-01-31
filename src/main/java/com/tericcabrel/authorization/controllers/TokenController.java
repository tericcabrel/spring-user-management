package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import static com.tericcabrel.authorization.utils.Constants.*;

import com.tericcabrel.authorization.models.common.*;
import com.tericcabrel.authorization.dtos.RefreshTokenDto;
import com.tericcabrel.authorization.dtos.ValidateTokenDto;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.models.redis.RefreshToken;
import com.tericcabrel.authorization.repositories.redis.RefreshTokenRepository;
import com.tericcabrel.authorization.services.interfaces.IUserService;
import com.tericcabrel.authorization.utils.JwtTokenUtil;

@Api(tags = "Token management", description = "Operations pertaining to token validation or refresh")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class TokenController {
    private Log logger = LogFactory.getLog(this.getClass());

    private JwtTokenUtil jwtTokenUtil;

    private RefreshTokenRepository refreshTokenRepository;

    private IUserService userService;

    public TokenController(
            JwtTokenUtil jwtTokenUtil, RefreshTokenRepository refreshTokenRepository, IUserService userService
    ) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @ApiOperation(value = "Validate a token", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The token is valid", response = SuccessResponse.class),
        @ApiResponse(code = 400, message = "Invalid token | The token has expired", response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/validate")
    public ResponseEntity<ServiceResponse> validate(@Valid @RequestBody ValidateTokenDto validateTokenDto) {
        String username = null;
        HashMap<String, String> result = new HashMap<>();

        try {
            username = jwtTokenUtil.getUsernameFromToken(validateTokenDto.getToken());
        } catch (IllegalArgumentException e) {
            logger.error(JWT_ILLEGAL_ARGUMENT_MESSAGE, e);
            result.put("message", "JWT_ILLEGAL_ARGUMENT_MESSAGE");
        } catch (ExpiredJwtException e) {
            logger.warn(JWT_EXPIRED_MESSAGE, e);
            result.put("message", "JWT_EXPIRED_MESSAGE");
        } catch(SignatureException e){
            logger.error(JWT_SIGNATURE_MESSAGE);
            result.put("message", "JWT_SIGNATURE_MESSAGE");
        }

        if (username != null) {
            result.put("message", "success");
            return ResponseEntity.ok(new ServiceResponse(200, result));
        }

        return ResponseEntity.badRequest().body(new ServiceResponse(400, result));
    }

    @ApiOperation(value = "Refresh token by generating new one", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New access token generated successfully", response = AuthTokenResponse.class),
        @ApiResponse(code = 400, message = "Invalid token | The token is unallocated", response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/refresh")
    public ResponseEntity<ServiceResponse> refresh(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByValue(refreshTokenDto.getToken());
        HashMap<String, String> result = new HashMap<>();

        if (refreshToken == null) {
            result.put("message", "The token is Invalid!");
            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        User user = userService.findById(refreshToken.getId());
        if (user == null) {
            result.put("message", "The token is unallocated!");
            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        String token = jwtTokenUtil.createTokenFromUser(user);
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);

        return ResponseEntity.ok(
            new ServiceResponse(HttpStatus.OK.value(), new AuthTokenResponse(token, refreshToken.getValue(), expirationDate.getTime()))
        );
    }
}
