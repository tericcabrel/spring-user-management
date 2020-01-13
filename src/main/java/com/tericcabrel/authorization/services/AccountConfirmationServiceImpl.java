package com.tericcabrel.authorization.services;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

import com.tericcabrel.authorization.models.AccountConfirmation;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.repositories.AccountConfirmationRepository;
import com.tericcabrel.authorization.services.interfaces.AccountConfirmationService;


@Service(value = "accountConfirmationService")
public class AccountConfirmationServiceImpl implements AccountConfirmationService {
    private AccountConfirmationRepository accountConfirmationRepository;

    public AccountConfirmationServiceImpl(AccountConfirmationRepository accountConfirmationRepository) {
        this.accountConfirmationRepository = accountConfirmationRepository;
    }

    @Override
    public AccountConfirmation save(User user, String token) {
        AccountConfirmation newAccountConfirmation = new AccountConfirmation();
        Date dateNow = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dateNow);
        c.add(Calendar.DATE, 2);

        newAccountConfirmation.setUser(user)
                .setToken(token)
                .setExpireAt(c.getTime().getTime())
                .setCreatedAt(dateNow)
                .setUpdatedAt(dateNow);

        return accountConfirmationRepository.save(newAccountConfirmation);
    }

    @Override
    public List<AccountConfirmation> findAll() {
        List<AccountConfirmation> list = new ArrayList<>();
        accountConfirmationRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public void delete(String id) {
        accountConfirmationRepository.deleteById(new ObjectId(id));
    }

    @Override
    public AccountConfirmation findByToken(String token) {
        return accountConfirmationRepository.findByToken(token);
    }

    @Override
    public AccountConfirmation findById(String id) {
        Optional<AccountConfirmation> optionalUser = accountConfirmationRepository.findById(new ObjectId(id));

        return optionalUser.orElse(null);
    }
}
