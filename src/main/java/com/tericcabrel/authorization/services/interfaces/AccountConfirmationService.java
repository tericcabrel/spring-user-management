package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.models.AccountConfirmation;
import com.tericcabrel.authorization.models.User;

import java.util.List;

public interface AccountConfirmationService {
    AccountConfirmation save(User user, String token);

    List<AccountConfirmation> findAll();

    void delete(String id);

    AccountConfirmation findByToken(String token);

    AccountConfirmation findById(String id);
}
