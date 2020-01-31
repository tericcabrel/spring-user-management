package com.tericcabrel.authorization.models.response;

import java.util.HashMap;

public class SuccessResponse {
    private int status;

    private HashMap<String, String> data;

    public SuccessResponse(int status, HashMap<String, String> data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> result) {
        this.data = result;
    }
}
