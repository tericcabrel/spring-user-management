package com.tericcabrel.authorization.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.tericcabrel.authorization.constraints.FieldMatch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ResetPasswordParam", description = "Parameters required to reset password")
@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
public class ResetPasswordDto {
    @ApiModelProperty(notes = "The token included in the reset link", required = true)
    @NotBlank(message = "The token is required")
    private String token;

    @ApiModelProperty(notes = "New value of the password", required = true)
    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String password;

    @ApiModelProperty(notes = "Confirmation of the new value of the password", required = true)
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
