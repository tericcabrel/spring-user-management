package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.mongo.Role;

public class RoleResponse {
    private int status;

    private Role data;

    public RoleResponse(int status, Role data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Role getData() {
        return data;
    }

    public void setData(Role result) {
        this.data = result;
    }
}
