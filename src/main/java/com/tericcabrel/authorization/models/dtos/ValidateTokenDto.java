package com.tericcabrel.authorization.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "ValidateTokenParam", description = "Parameters required to perform a token validation")
@Accessors(chain = true)
@Setter
@Getter
public class ValidateTokenDto {
    @ApiModelProperty(notes = "Token to validate", required = true)
    @NotBlank(message = "The token is required")
    private String token;
}
