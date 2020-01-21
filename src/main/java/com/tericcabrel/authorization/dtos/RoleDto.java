package com.tericcabrel.authorization.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "CreateRoleParam", description = "Parameters required to create role")
public class RoleDto {
    @ApiModelProperty(notes = "Name of the role", required = true)
    @NotBlank(message = "The name is required")
    private String name;

    @ApiModelProperty(notes = "Description of the role")
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
