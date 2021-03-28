package com.tericcabrel.authorization.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class ServiceResponse {
    private int status;

    private Object data;
}
