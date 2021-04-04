package com.tericcabrel.authorization.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "UpdateRoleParam", description = "Parameters required to update the roles of an user")
@Accessors(chain = true)
@Setter
@Getter
public class UpdateRoleDto {
    @ApiModelProperty(notes = "User identifier", required = true)
    @NotBlank(message = "The userId is required")
    private String userId;

    @ApiModelProperty(notes = "Array of roles to give to an user", required = true)
    @NotEmpty(message = "The field must have at least one item")
    private String[] roles;
}
