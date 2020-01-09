package com.tericcabrel.authorization.models;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts_confirmation")
public class AccountConfirmation extends BaseModel {
    @DBRef
    private User user;

    private String token;

    private long expireAt;

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
