package com.tericcabrel.authorization.dtos;

import com.tericcabrel.authorization.constraints.FieldMatch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
public class ResetPasswordDto {
    @NotBlank(message = "The token is required")
    private String token;

    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String password;

    @NotBlank(message = "This field is required")
    private String confirmPassword;

    public String getToken() {
        return token;
    }

    public ResetPasswordDto setToken(String token) {
        this.token = token;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ResetPasswordDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public ResetPasswordDto setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
