package com.tericcabrel.authorization.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ForgotPasswordDto {
    @Email(message = "Email address is not valid")
    @NotBlank(message = "The email address is required")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
