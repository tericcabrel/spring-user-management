package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.mongo.Role;

import java.util.Set;

public class RoleListResponse {
    private int status;

    private Set<Role> data;

    public RoleListResponse(int status, Set<Role> data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Role> getData() {
        return data;
    }

    public void setData(Set<Role> result) {
        this.data = result;
    }
}
