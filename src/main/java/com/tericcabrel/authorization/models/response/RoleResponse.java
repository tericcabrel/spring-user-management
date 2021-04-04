package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RoleResponse {
    private Role data;
}
