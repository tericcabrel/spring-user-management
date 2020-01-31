package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;

import static com.tericcabrel.authorization.utils.Constants.INVALID_DATA_MESSAGE;

import com.tericcabrel.authorization.models.common.BadRequestResponse;
import com.tericcabrel.authorization.models.common.InvalidDataResponse;
import com.tericcabrel.authorization.models.common.SuccessResponse;
import com.tericcabrel.authorization.dtos.*;
import com.tericcabrel.authorization.models.ResetPassword;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.models.common.ServiceResponse;
import com.tericcabrel.authorization.services.interfaces.IResetPasswordService;
import com.tericcabrel.authorization.services.interfaces.IUserService;
import com.tericcabrel.authorization.events.OnResetPasswordEvent;


@Api(tags = "Password reset management", description = "Operations pertaining to user's reset password process")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class ResetPasswordController {

    private IUserService userService;

    private ApplicationEventPublisher eventPublisher;

    private IResetPasswordService resetPasswordService;

    public ResetPasswordController(
        IUserService userService,
        ApplicationEventPublisher eventPublisher,
        IResetPasswordService resetPasswordService
    ) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.resetPasswordService = resetPasswordService;
    }

    @ApiOperation(value = "Request a link to reset the password", response = ServiceResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reset link sent to the mail box successfully!", response = SuccessResponse.class),
            @ApiResponse(code = 400, message = "No user found with the email provided", response = BadRequestResponse.class),
            @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/forgot-password")
    public ResponseEntity<ServiceResponse> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        User user = userService.findByEmail(forgotPasswordDto.getEmail());
        HashMap<String, String> result = new HashMap<>();

        if (user == null) {
            result.put("message", "No user found with this email!");

            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        eventPublisher.publishEvent(new OnResetPasswordEvent(user));

        result.put("message", "A password reset link has been sent to your email box!");

        return ResponseEntity.ok(new ServiceResponse(HttpStatus.OK.value(), result));
    }

    @ApiOperation(value = "Change the user password through a reset token", response = ServiceResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The action completed successfully!", response = SuccessResponse.class),
            @ApiResponse(code = 400, message = "The token is invalid or has expired", response = BadRequestResponse.class),
            @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping(value = "/reset-password")
    public ResponseEntity<ServiceResponse> resetPassword(@Valid @RequestBody ResetPasswordDto passwordResetDto) {
        ResetPassword resetPassword = resetPasswordService.findByToken(passwordResetDto.getToken());
        HashMap<String, String> result = new HashMap<>();

        if (resetPassword == null) {
            result.put("message", "The token is invalid!");

            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        if (resetPassword.getExpireAt() < new Date().getTime()) {
            result.put("message", "You token has been expired!");

            resetPasswordService.delete(resetPassword.getId());

            return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        userService.updatePassword(resetPassword.getUser().getId(), passwordResetDto.getPassword());

        result.put("message", "Your password has been resetted successfully!");

        // Avoid the user or malicious person to reuse the link to change the password
        resetPasswordService.delete(resetPassword.getId());

        return ResponseEntity.badRequest().body(new ServiceResponse(HttpStatus.OK.value(), result));
    }
}
