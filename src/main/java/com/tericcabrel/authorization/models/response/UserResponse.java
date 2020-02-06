package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.mongo.User;

public class UserResponse {
    private int status;

    private User data;

    public UserResponse(int status, User data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getData() {
        return data;
    }

    public void setData(User result) {
        this.data = result;
    }
}
