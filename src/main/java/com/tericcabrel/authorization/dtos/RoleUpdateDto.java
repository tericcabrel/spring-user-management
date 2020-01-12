package com.tericcabrel.authorization.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class RoleUpdateDto {
    @NotBlank(message = "The userId is required")
    private String userId;

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
