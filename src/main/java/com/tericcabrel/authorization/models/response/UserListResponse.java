package com.tericcabrel.authorization.models.response;

import com.tericcabrel.authorization.models.entities.User;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserListResponse {
    private List<User> data;
}
