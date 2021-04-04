package com.tericcabrel.authorization.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.tericcabrel.authorization.constraints.FieldMatch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "ResetPasswordParam", description = "Parameters required to reset password")
@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
@Accessors(chain = true)
@Setter
@Getter
public class ResetPasswordDto {
    @ApiModelProperty(notes = "The token included in the reset link", required = true)
    @NotBlank(message = "The token is required")
    private String token;

    @ApiModelProperty(notes = "New value of the password", required = true)
    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String password;

    @ApiModelProperty(notes = "Confirmation of the new value of the password", required = true)
    @NotBlank(message = "This field is required")
    private String confirmPassword;
}
