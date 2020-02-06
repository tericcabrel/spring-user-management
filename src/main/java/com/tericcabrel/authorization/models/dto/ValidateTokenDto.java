package com.tericcabrel.authorization.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "ValidateTokenParam", description = "Parameters required to perform a token validation")
public class ValidateTokenDto {
    @ApiModelProperty(notes = "Token to validate", required = true)
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
