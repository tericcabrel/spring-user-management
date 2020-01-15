package com.tericcabrel.authorization.services;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

import com.tericcabrel.authorization.models.ConfirmAccount;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.repositories.ConfirmAccountRepository;
import com.tericcabrel.authorization.services.interfaces.ConfirmAccountService;


@Service(value = "confirmAccountService")
public class ConfirmAccountServiceImpl implements ConfirmAccountService {
    private ConfirmAccountRepository confirmAccountRepository;

    public ConfirmAccountServiceImpl(ConfirmAccountRepository confirmAccountRepository) {
        this.confirmAccountRepository = confirmAccountRepository;
    }

    @Override
    public ConfirmAccount save(User user, String token) {
        ConfirmAccount newConfirmAccount = new ConfirmAccount();
        Date dateNow = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dateNow);
        c.add(Calendar.DATE, 2);

        newConfirmAccount.setUser(user)
                .setToken(token)
                .setExpireAt(c.getTime().getTime());

        return confirmAccountRepository.save(newConfirmAccount);
    }

    @Override
    public List<ConfirmAccount> findAll() {
        List<ConfirmAccount> list = new ArrayList<>();
        confirmAccountRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public void delete(String id) {
        confirmAccountRepository.deleteById(new ObjectId(id));
    }

    @Override
    public ConfirmAccount findByToken(String token) {
        return confirmAccountRepository.findByToken(token);
    }

    @Override
    public ConfirmAccount findById(String id) {
        Optional<ConfirmAccount> optionalUser = confirmAccountRepository.findById(new ObjectId(id));

        return optionalUser.orElse(null);
    }
}
