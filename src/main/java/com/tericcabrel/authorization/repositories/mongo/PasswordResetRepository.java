package com.tericcabrel.authorization.repositories.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tericcabrel.authorization.models.ResetPassword;

@Repository
public interface PasswordResetRepository extends MongoRepository<ResetPassword, ObjectId> {
    ResetPassword findByToken(String token);
}
