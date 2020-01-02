package com.tericcabrel.authorization.dtos;

import javax.validation.constraints.NotBlank;

public class RoleDto {
    @NotBlank(message = "The name is required")
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public RoleDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoleDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
