package com.tericcabrel.authorization.repositories;

import com.tericcabrel.authorization.models.PasswordReset;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordResetRepository extends MongoRepository<PasswordReset, ObjectId> {
    PasswordReset findByToken(String token);
}
