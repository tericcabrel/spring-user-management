package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.models.PasswordReset;
import com.tericcabrel.authorization.models.User;

import java.util.List;

public interface PasswordResetService {
    PasswordReset save(User user, String token);

    List<PasswordReset> findAll();

    void delete(String id);

    PasswordReset findByToken(String token);

    PasswordReset findById(String id);
}
