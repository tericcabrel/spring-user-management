package com.tericcabrel.authorization.models.response;

import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class InvalidDataResponse {
    private int status;

    private HashMap<String, HashMap<String, List<String>>> data;
}
