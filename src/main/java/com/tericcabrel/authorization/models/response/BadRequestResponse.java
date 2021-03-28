package com.tericcabrel.authorization.models.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class BadRequestResponse {
    private int status;

    private Map<String, String> data;
}
