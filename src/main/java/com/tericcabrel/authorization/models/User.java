package com.tericcabrel.authorization.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.ZonedDateTime;
import java.util.Set;

@Document(collection = "users")
public class User {
    @Id
    private ObjectId _id;

    private String firstName;

    private String lastName;

    private String gender;

    @Field("email")
    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private boolean enabled;

    private boolean confirmed;

    private String avatar;

    private Coordinates coordinates;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @DBRef
    private Set<Role> roles;

    public User(String firstName, String lastName, String email, String password, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.enabled = true;
        this.confirmed = false;
    }

    public ObjectId get_id() {
        return _id;
    }

    public User set_id(ObjectId _id) {
        this._id = _id;
        return this;
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public User setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
