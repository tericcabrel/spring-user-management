package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.mongo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class UserResponse {
    private int status;

    private User data;
}
