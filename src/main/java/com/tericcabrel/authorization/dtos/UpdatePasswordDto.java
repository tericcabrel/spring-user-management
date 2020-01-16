package com.tericcabrel.authorization.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value = "UpdatePasswordParam", description = "Parameters required to update the password")
public class UpdatePasswordDto {
    @ApiModelProperty(notes = "Current user password", required = true)
    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String currentPassword;

    @ApiModelProperty(notes = "New user password", required = true)
    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public UpdatePasswordDto setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public UpdatePasswordDto setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }
}
