package com.tericcabrel.authorization.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class User extends BaseModel {
    private String firstName;

    private String lastName;

    private String gender;

    @Field("email")
    private String email;

    @JsonIgnore
    private String password;

    private boolean enabled;

    private boolean confirmed;

    private String avatar;

    private String timezone;

    private Coordinates coordinates;

    @DBRef
    private Set<Role> roles;

    public User() {
        roles = new HashSet<>();
    }

    public User(String firstName, String lastName, String email, String password, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.enabled = true;
        this.confirmed = false;
        roles = new HashSet<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public User setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getTimezone() {
        return timezone;
    }

    public User setTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public User setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User addRole(Role role) {
        this.roles.add(role);

        return this;
    }
}
