package com.tericcabrel.authorization.models.dtos;

import com.tericcabrel.authorization.constraints.Exists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "ForgotPasswordParam", description = "Parameters required to request a reset link")
@Exists.List({
    @Exists(property = "email", repository = "UserRepository", message = "This email doesn't exists in the db!")
})
@Accessors(chain = true)
@Setter
@Getter
public class ForgotPasswordDto {
    @ApiModelProperty(notes = "The email address to sent the link to", required = true)
    @Email(message = "Email address is not valid")
    @NotBlank(message = "The email address is required")
    private String email;
}
