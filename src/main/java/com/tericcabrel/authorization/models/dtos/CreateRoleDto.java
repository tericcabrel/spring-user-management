package com.tericcabrel.authorization.models.dtos;

import com.tericcabrel.authorization.models.entities.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "CreateRoleParam", description = "Parameters required to create role")
@Accessors(chain = true)
@Setter
@Getter
public class CreateRoleDto {
    @ApiModelProperty(notes = "Name of the role", required = true)
    @NotBlank(message = "The name is required")
    private String name;

    @ApiModelProperty(notes = "Description of the role")
    private String description;

    private boolean isDefault;

    public Role toRole() {
        return new Role()
            .setName(this.name)
            .setDescription(this.description)
            .setDefault(this.isDefault);
    }
}
