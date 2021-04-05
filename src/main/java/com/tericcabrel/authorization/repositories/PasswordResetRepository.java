package com.tericcabrel.authorization.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tericcabrel.authorization.models.entities.ResetPassword;

@Repository
public interface PasswordResetRepository extends MongoRepository<ResetPassword, ObjectId> {
    ResetPassword findByToken(String token);
}
