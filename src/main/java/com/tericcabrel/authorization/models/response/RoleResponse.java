package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.mongo.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class RoleResponse {
    private int status;

    private Role data;
}
