package com.tericcabrel.authorization.services;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

import com.tericcabrel.authorization.models.ResetPassword;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.repositories.PasswordResetRepository;
import com.tericcabrel.authorization.services.interfaces.IResetPasswordService;

@Service(value = "passwordResetService")
public class ResetPasswordService implements IResetPasswordService {
    private PasswordResetRepository passwordResetRepository;

    public ResetPasswordService(PasswordResetRepository passwordResetRepository) {
        this.passwordResetRepository = passwordResetRepository;
    }

    @Override
    public ResetPassword save(User user, String token) {
        ResetPassword newResetPassword = new ResetPassword();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 2);

        newResetPassword.setUser(user)
                .setToken(token)
                .setExpireAt(c.getTime().getTime());

        return passwordResetRepository.save(newResetPassword);
    }

    @Override
    public List<ResetPassword> findAll() {
        List<ResetPassword> list = new ArrayList<>();
        passwordResetRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public void delete(String id) {
        passwordResetRepository.deleteById(new ObjectId(id));
    }

    @Override
    public ResetPassword findByToken(String token) {
        return passwordResetRepository.findByToken(token);
    }

    @Override
    public ResetPassword findById(String id) {
        Optional<ResetPassword> optionalPasswordReset = passwordResetRepository.findById(new ObjectId(id));

        return optionalPasswordReset.orElse(null);
    }
}
