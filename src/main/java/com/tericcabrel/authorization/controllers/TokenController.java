package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.exceptions.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import static com.tericcabrel.authorization.utils.Constants.*;

import com.tericcabrel.authorization.models.response.*;
import com.tericcabrel.authorization.models.dtos.RefreshTokenDto;
import com.tericcabrel.authorization.models.dtos.ValidateTokenDto;
import com.tericcabrel.authorization.models.entities.User;
import com.tericcabrel.authorization.models.entities.RefreshToken;
import com.tericcabrel.authorization.repositories.RefreshTokenRepository;
import com.tericcabrel.authorization.services.interfaces.UserService;
import com.tericcabrel.authorization.utils.JwtTokenUtil;


@Api(tags = SWG_TOKEN_TAG_NAME, description = SWG_TOKEN_TAG_DESCRIPTION)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class TokenController {

  private final Log logger = LogFactory.getLog(this.getClass());

  private final JwtTokenUtil jwtTokenUtil;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserService userService;

  public TokenController(
      JwtTokenUtil jwtTokenUtil,
      RefreshTokenRepository refreshTokenRepository,
      UserService userService
  ) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.refreshTokenRepository = refreshTokenRepository;
    this.userService = userService;
  }

  @ApiOperation(value = SWG_TOKEN_VALIDATE_OPERATION, response = SuccessResponse.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = SWG_TOKEN_VALIDATE_MESSAGE, response = SuccessResponse.class),
      @ApiResponse(code = 400, message = SWG_TOKEN_VALIDATE_ERROR, response = BadRequestResponse.class),
      @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
  })
  @PostMapping(value = "/validate")
  public ResponseEntity<Map<String, String>> validate(@Valid @RequestBody ValidateTokenDto validateTokenDto) {
    String username = null;
    Map<String, String> result = new HashMap<>();

    try {
      username = jwtTokenUtil.getUsernameFromToken(validateTokenDto.getToken());
    } catch (IllegalArgumentException e) {
      logger.error(JWT_ILLEGAL_ARGUMENT_MESSAGE, e);
      result.put(MESSAGE_KEY, JWT_ILLEGAL_ARGUMENT_MESSAGE);
    } catch (ExpiredJwtException e) {
      logger.warn(JWT_EXPIRED_MESSAGE, e);
      result.put(MESSAGE_KEY, JWT_EXPIRED_MESSAGE);
    } catch (SignatureException e) {
      logger.error(JWT_SIGNATURE_MESSAGE);
      result.put(MESSAGE_KEY, JWT_SIGNATURE_MESSAGE);
    }

    if (username != null) {
      result.put(MESSAGE_KEY, VALIDATE_TOKEN_SUCCESS_MESSAGE);
      return ResponseEntity.ok(result);
    }

    return ResponseEntity.badRequest().body(result);
  }

  @ApiOperation(value = SWG_TOKEN_REFRESH_OPERATION, response = SuccessResponse.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = SWG_TOKEN_REFRESH_MESSAGE, response = AuthTokenResponse.class),
      @ApiResponse(code = 400, message = SWG_TOKEN_REFRESH_ERROR, response = SuccessResponse.class),
      @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
  })
  @PostMapping(value = "/refresh")
  public ResponseEntity<Object> refresh(@Valid @RequestBody RefreshTokenDto refreshTokenDto)
      throws ResourceNotFoundException {
    RefreshToken refreshToken = refreshTokenRepository.findByValue(refreshTokenDto.getToken());
    Map<String, String> result = new HashMap<>();

    if (refreshToken == null) {
      result.put(MESSAGE_KEY, INVALID_TOKEN_MESSAGE);
      return ResponseEntity.badRequest().body(result);
    }

    User user = userService.findById(refreshToken.getId());
    if (user == null) {
      result.put(MESSAGE_KEY, TOKEN_NOT_FOUND_MESSAGE);
      return ResponseEntity.badRequest().body(result);
    }

    String token = jwtTokenUtil.createTokenFromUser(user);
    Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);

    return ResponseEntity.ok(new AuthTokenResponse(token, refreshToken.getValue(), expirationDate.getTime()));
  }
}
