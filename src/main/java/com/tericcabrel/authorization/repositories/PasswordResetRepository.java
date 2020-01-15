package com.tericcabrel.authorization.repositories;

import com.tericcabrel.authorization.models.ResetPassword;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordResetRepository extends MongoRepository<ResetPassword, ObjectId> {
    ResetPassword findByToken(String token);
}
