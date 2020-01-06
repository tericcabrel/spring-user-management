package com.tericcabrel.authorization.dtos;

import javax.validation.constraints.NotBlank;

public class ValidateTokenDto {
    @NotBlank(message = "The token is required")
    private String token;

    public String getToken() {
        return token;
    }

    public ValidateTokenDto setToken(String token) {
        this.token = token;
        return this;
    }
}
