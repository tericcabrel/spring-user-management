package com.tericcabrel.authorization.models.common;

public class ApiResponse {
    private int status;

    private Object data;

    public ApiResponse(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object result) {
        this.data = result;
    }
}
