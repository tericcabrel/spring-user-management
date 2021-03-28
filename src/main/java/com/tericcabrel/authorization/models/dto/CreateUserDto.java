package com.tericcabrel.authorization.models.dto;

import javax.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

import com.tericcabrel.authorization.constraints.IsUnique;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.tericcabrel.authorization.models.mongo.Coordinates;
import com.tericcabrel.authorization.models.mongo.Role;
import com.tericcabrel.authorization.constraints.FieldMatch;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@ApiModel(value = "RegisterParam", description = "Parameters required to create or update user")
@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
@IsUnique.List({
    @IsUnique(property = "email", repository = "UserRepository", message = "This email already exists!")
})
@Accessors(chain = true)
@Setter
@Getter
public class CreateUserDto {
    @ApiModelProperty(hidden = true)
    private String id;

    @ApiModelProperty(notes = "User first name", required = true)
    @NotBlank(message = "The first name is required")
    private String firstName;

    @ApiModelProperty(notes = "User last name", required = true)
    @NotBlank(message = "The last name is required")
    private String lastName;

    @ApiModelProperty(notes = "User email address", required = true)
    @Email(message = "Email address is not valid")
    @NotBlank(message = "The email address is required")
    private String email;

    @ApiModelProperty(notes = "User's password (must be at least 6 characters)", required = true)
    @Size(min = 6, message = "Must be at least 6 characters")
    private String password;

    @ApiModelProperty(notes = "User timezone", required = true)
    @NotBlank(message = "The timezone is required")
    private String timezone;

    @ApiModelProperty(notes = "Password confirmation", required = true)
    @NotBlank(message = "This field is required")
    private String confirmPassword;

    @ApiModelProperty(notes = "User gender")
    private String gender;

    private String avatar;

    @ApiModelProperty(notes = "Indicates if the will be enabled or not")
    private boolean enabled;

    @ApiModelProperty(notes = "Indicates if has confirmed his account")
    private boolean confirmed;

    @ApiModelProperty(notes = "Geographic location of the user")
    private Coordinates coordinates;

    @ApiModelProperty(hidden = true)
    private Set<Role> roles;

    public CreateUserDto() {
        enabled = true;
        roles = new HashSet<>();
    }
}
