package com.tericcabrel.authorization.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "UpdateRoleParam", description = "Parameters required to update the roles of an user")
public class RoleUpdateDto {
    @ApiModelProperty(notes = "User identifier", required = true)
    @NotBlank(message = "The userId is required")
    private String userId;

    @ApiModelProperty(notes = "Array of roles to give to an user", required = true)
    @NotEmpty(message = "The field must have at least one item")
    private String[] roles;

    public String getUserId() {
        return userId;
    }

    public RoleUpdateDto setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String[] getRoles() {
        return roles;
    }

    public RoleUpdateDto setRoles(String[] roles) {
        this.roles = roles;
        return this;
    }
}
