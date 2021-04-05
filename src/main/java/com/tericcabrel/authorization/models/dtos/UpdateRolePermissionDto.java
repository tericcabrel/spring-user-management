package com.tericcabrel.authorization.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "UpdateRolePermissionParam", description = "Parameters required to update role permissions")
@Accessors(chain = true)
@Setter
@Getter
public class UpdateRolePermissionDto {
    @ApiModelProperty(notes = "Array of permissions to give or remove to a role", required = true)
    @NotEmpty(message = "The field must have at least one item")
    private String[] permissions;
}
