package com.tericcabrel.authorization.models.common;

public class AuthToken {
    private String token;
    private long expiresIn;

    public AuthToken(String token, long expiresIn){
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public AuthToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}


