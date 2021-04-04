package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserResponse {
    private User data;
}
