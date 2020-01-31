package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.models.mongo.ResetPassword;
import com.tericcabrel.authorization.models.mongo.User;

import java.util.List;

public interface IResetPasswordService {
    ResetPassword save(User user, String token);

    List<ResetPassword> findAll();

    void delete(String id);

    ResetPassword findByToken(String token);

    ResetPassword findById(String id);
}
