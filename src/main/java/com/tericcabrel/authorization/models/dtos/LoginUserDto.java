package com.tericcabrel.authorization.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "LoginUserParam", description = "Parameters required to login user")
@Accessors(chain = true)
@Setter
@Getter
public class LoginUserDto {
    @ApiModelProperty(notes = "User email address", required = true)
    @Email(message = "Email address is not valid")
    @NotBlank(message = "The email address is required")
    private String email;

    @ApiModelProperty(notes = "User password (Min character: 6)", required = true)
    @Size(min = 6, message = "Must be at least 6 characters")
    private String password;
}
