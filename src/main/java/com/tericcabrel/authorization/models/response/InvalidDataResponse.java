package com.tericcabrel.authorization.models.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class InvalidDataResponse {
    private Map<String, Map<String, List<String>>> data;
}
