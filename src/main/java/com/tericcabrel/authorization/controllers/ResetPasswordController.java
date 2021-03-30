package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;

import static com.tericcabrel.authorization.utils.Constants.INVALID_DATA_MESSAGE;
import static com.tericcabrel.authorization.utils.Constants.INVALID_TOKEN_MESSAGE;
import static com.tericcabrel.authorization.utils.Constants.MESSAGE_KEY;
import static com.tericcabrel.authorization.utils.Constants.TOKEN_EXPIRED_MESSAGE;

import com.tericcabrel.authorization.models.dto.ForgotPasswordDto;
import com.tericcabrel.authorization.models.dto.ResetPasswordDto;
import com.tericcabrel.authorization.models.response.InvalidDataResponse;
import com.tericcabrel.authorization.models.mongo.ResetPassword;
import com.tericcabrel.authorization.models.mongo.User;
import com.tericcabrel.authorization.models.response.GenericResponse;
import com.tericcabrel.authorization.services.interfaces.IResetPasswordService;
import com.tericcabrel.authorization.services.interfaces.IUserService;
import com.tericcabrel.authorization.events.OnResetPasswordEvent;


@Api(tags = "Password reset management", description = "Operations pertaining to user's reset password process")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class ResetPasswordController {

    private final IUserService userService;

    private final ApplicationEventPublisher eventPublisher;

    private final IResetPasswordService resetPasswordService;

    public ResetPasswordController(
        IUserService userService,
        ApplicationEventPublisher eventPublisher,
        IResetPasswordService resetPasswordService
    ) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.resetPasswordService = resetPasswordService;
    }

    @ApiOperation(value = "Request a link to reset the password", response = GenericResponse.class)
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Reset link sent to the mail box successfully!", response = GenericResponse.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "No user found with the email provided", response = GenericResponse.class),
            @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        User user = userService.findByEmail(forgotPasswordDto.getEmail());
        Map<String, String> result = new HashMap<>();

        if (user == null) {
            result.put(MESSAGE_KEY, "No user found with this email!");

            return ResponseEntity.badRequest().body(result);
        }

        eventPublisher.publishEvent(new OnResetPasswordEvent(user));

        result.put(MESSAGE_KEY, "A password reset link has been sent to your email box!");

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Change the user password through a reset token", response = GenericResponse.class)
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "The action completed successfully!", response = GenericResponse.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "The token is invalid or has expired", response = GenericResponse.class),
            @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordDto passwordResetDto) {
        ResetPassword resetPassword = resetPasswordService.findByToken(passwordResetDto.getToken());
        Map<String, String> result = new HashMap<>();

        if (resetPassword == null) {
            result.put(MESSAGE_KEY, INVALID_TOKEN_MESSAGE);

            return ResponseEntity.badRequest().body(result);
        }

        if (resetPassword.getExpireAt() < new Date().getTime()) {
            result.put(MESSAGE_KEY, TOKEN_EXPIRED_MESSAGE);

            resetPasswordService.delete(resetPassword.getId());

            return ResponseEntity.badRequest().body(result);
        }

        userService.updatePassword(resetPassword.getUser().getId(), passwordResetDto.getPassword());

        result.put(MESSAGE_KEY, "Your password has been resetted successfully!");

        // Avoid the user or malicious person to reuse the link to change the password
        resetPasswordService.delete(resetPassword.getId());

        return ResponseEntity.badRequest().body(result);
    }
}
