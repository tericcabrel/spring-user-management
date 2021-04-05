package com.tericcabrel.authorization.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "UpdateUserPermissionParam", description = "Parameters required to update user permissions")
@Accessors(chain = true)
@Setter
@Getter
public class UpdateUserPermissionDto {
    @ApiModelProperty(notes = "Array of permissions to give or remove to an user", required = true)
    @NotEmpty(message = "The field must have at least one item")
    private String[] permissions;
}
