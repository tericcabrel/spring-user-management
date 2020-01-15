package com.tericcabrel.authorization.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tericcabrel.authorization.models.ResetPassword;

public interface PasswordResetRepository extends MongoRepository<ResetPassword, ObjectId> {
    ResetPassword findByToken(String token);
}
