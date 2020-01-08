package com.tericcabrel.authorization.dtos;

import com.tericcabrel.authorization.models.Coordinates;
import com.tericcabrel.authorization.models.Role;

import java.util.HashSet;
import java.util.Set;

public class UpdateUserDto {
    private String firstName;

    private String lastName;

    private String timezone;

    private String gender;

    private boolean enabled;

    private boolean confirmed;

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
