package com.tericcabrel.authorization.controllers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;

import com.tericcabrel.authorization.dtos.*;
import com.tericcabrel.authorization.models.ResetPassword;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.models.common.ServiceResponse;
import com.tericcabrel.authorization.services.interfaces.IResetPasswordService;
import com.tericcabrel.authorization.services.interfaces.IUserService;
import com.tericcabrel.authorization.events.OnResetPasswordEvent;

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
