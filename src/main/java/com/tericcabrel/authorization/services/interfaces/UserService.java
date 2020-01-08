package com.tericcabrel.authorization.services.interfaces;

import java.util.List;

import com.tericcabrel.authorization.dtos.UpdatePasswordDto;
import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.User;

public interface UserService {
    User save(UserDto userDto);

    List<User> findAll();

    void delete(String id);

    User findByEmail(String email);

    User findById(String id);

    User update(String id, UserDto userDto);

    void update(User user);

    User updatePassword(String id, UpdatePasswordDto updatePasswordDto);
}
