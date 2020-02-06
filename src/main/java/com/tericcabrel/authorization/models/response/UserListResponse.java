package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.mongo.User;

import java.util.Set;

public class UserListResponse {
    private int status;

    private Set<User> data;

    public UserListResponse(int status, Set<User> data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<User> getData() {
        return data;
    }

    public void setData(Set<User> result) {
        this.data = result;
    }
}
