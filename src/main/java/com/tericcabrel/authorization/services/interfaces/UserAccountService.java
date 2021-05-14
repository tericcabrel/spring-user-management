package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.exceptions.ResourceNotFoundException;
import com.tericcabrel.authorization.models.entities.UserAccount;
import com.tericcabrel.authorization.models.entities.User;

import java.util.List;

public interface UserAccountService {
    UserAccount save(User user, String token);

    List<UserAccount> findAll();

    void delete(String id);

    UserAccount findByToken(String token) throws ResourceNotFoundException;

    UserAccount findById(String id) throws ResourceNotFoundException;
}
