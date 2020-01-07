package com.tericcabrel.authorization.dtos;

import javax.validation.constraints.NotBlank;

public class RefreshTokenDto {
    @NotBlank(message = "The token is required")
    private String token;

    public String getToken() {
        return token;
    }

    public RefreshTokenDto setToken(String token) {
        this.token = token;
        return this;
    }
}
