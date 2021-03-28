package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.mongo.User;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class UserListResponse {
    private int status;

    private Set<User> data;
}
