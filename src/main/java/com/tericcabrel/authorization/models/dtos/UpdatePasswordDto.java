package com.tericcabrel.authorization.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "UpdatePasswordParam", description = "Parameters required to update the password")
@Accessors(chain = true)
@Setter
@Getter
public class UpdatePasswordDto {
    @ApiModelProperty(notes = "Current user password", required = true)
    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String currentPassword;

    @ApiModelProperty(notes = "New user password", required = true)
    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String newPassword;
}
