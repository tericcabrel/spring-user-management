package com.tericcabrel.authorization.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Set;

import com.tericcabrel.authorization.models.mongo.Coordinates;
import com.tericcabrel.authorization.models.mongo.Role;

@ApiModel(value = "UpdateUserParam", description = "Parameters required to update an user")
public class UpdateUserDto {
    @ApiModelProperty(notes = "User first name")
    private String firstName;

    @ApiModelProperty(notes = "User last name")
    private String lastName;

    @ApiModelProperty(notes = "User timezone")
    private String timezone;

    @ApiModelProperty(notes = "User gender")
    private String gender;

    private String avatar;

    @ApiModelProperty(notes = "Indicates if the will be enabled or not")
    private boolean enabled;

    @ApiModelProperty(notes = "Indicates if has confirmed his account")
    private boolean confirmed;

    @ApiModelProperty(notes = "Geographic location of the user")
    private Coordinates coordinates;

    private Set<Role> roles;

    public UpdateUserDto() {
        roles = new HashSet<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public UpdateUserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UpdateUserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getTimezone() {
        return timezone;
    }

    public UpdateUserDto setTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }


    public String getGender() {
        return gender;
    }

    public UpdateUserDto setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public UpdateUserDto setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UpdateUserDto setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public UpdateUserDto setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public UpdateUserDto setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public UpdateUserDto setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }
}
