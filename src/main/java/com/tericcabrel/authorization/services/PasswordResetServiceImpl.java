package com.tericcabrel.authorization.services;

import com.tericcabrel.authorization.models.PasswordReset;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.repositories.PasswordResetRepository;
import com.tericcabrel.authorization.services.interfaces.PasswordResetService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;


@Service(value = "passwordResetService")
public class PasswordResetServiceImpl implements PasswordResetService {
    private PasswordResetRepository passwordResetRepository;

    public PasswordResetServiceImpl(PasswordResetRepository passwordResetRepository) {
        this.passwordResetRepository = passwordResetRepository;
    }

    @Override
    public PasswordReset save(User user, String token) {
        PasswordReset newPasswordReset = new PasswordReset();

        Date dateNow = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dateNow);
        c.add(Calendar.DATE, 2);

        newPasswordReset.setUser(user)
                .setToken(token)
                .setExpireAt(c.getTime().getTime())
                .setCreatedAt(dateNow)
                .setUpdatedAt(dateNow);

        return passwordResetRepository.save(newPasswordReset);
    }

    @Override
    public List<PasswordReset> findAll() {
        List<PasswordReset> list = new ArrayList<>();
        passwordResetRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public void delete(String id) {
        passwordResetRepository.deleteById(new ObjectId(id));
    }

    @Override
    public PasswordReset findByToken(String token) {
        return passwordResetRepository.findByToken(token);
    }

    @Override
    public PasswordReset findById(String id) {
        Optional<PasswordReset> optionalPasswordReset = passwordResetRepository.findById(new ObjectId(id));

        return optionalPasswordReset.orElse(null);
    }
}
