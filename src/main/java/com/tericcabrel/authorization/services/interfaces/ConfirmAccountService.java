package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.models.ConfirmAccount;
import com.tericcabrel.authorization.models.User;

import java.util.List;

public interface ConfirmAccountService {
    ConfirmAccount save(User user, String token);

    List<ConfirmAccount> findAll();

    void delete(String id);

    ConfirmAccount findByToken(String token);

    ConfirmAccount findById(String id);
}
