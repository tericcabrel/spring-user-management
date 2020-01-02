package com.tericcabrel.authorization.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "password_resets")
public class PasswordReset {
    @Id
    private ObjectId _id;

    @DBRef
    private User user;

    private String token;

    private long expireAt;

    public ObjectId get_id() {
        return _id;
    }

    public PasswordReset set_id(ObjectId _id) {
        this._id = _id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public PasswordReset setUser(User user) {
        this.user = user;
        return this;
    }

    public String getToken() {
        return token;
    }

    public PasswordReset setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public PasswordReset setExpireAt(long expireAt) {
        this.expireAt = expireAt;
        return this;
    }
}
