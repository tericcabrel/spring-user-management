package com.tericcabrel.authorization.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts_confirmation")
public class AccountConfirmation {
    @Id
    private ObjectId _id;

    @DBRef
    private User user;

    private String token;

    private long expireAt;

    public ObjectId get_id() {
        return _id;
    }

    public AccountConfirmation set_id(ObjectId _id) {
        this._id = _id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public AccountConfirmation setUser(User user) {
        this.user = user;
        return this;
    }

    public String getToken() {
        return token;
    }

    public AccountConfirmation setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public AccountConfirmation setExpireAt(long expireAt) {
        this.expireAt = expireAt;
        return this;
    }
}
