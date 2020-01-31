package com.tericcabrel.authorization.models.response;

import java.util.HashMap;
import java.util.List;

public class InvalidDataResponse {
    private int status;

    private HashMap<String, HashMap<String, List<String>>> data;

    public InvalidDataResponse(int status, HashMap<String, HashMap<String, List<String>>> data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, HashMap<String, List<String>>> getData() {
        return data;
    }

    public void setData(HashMap<String, HashMap<String, List<String>>> result) {
        this.data = result;
    }
}
