package com.tericcabrel.authorization.models.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class BadRequestResponse {
    private Map<String, String> data;
}
