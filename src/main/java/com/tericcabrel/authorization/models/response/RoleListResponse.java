package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.mongo.Role;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class RoleListResponse {
    private int status;

    private Set<Role> data;
}
