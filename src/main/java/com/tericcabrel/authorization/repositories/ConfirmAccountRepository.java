package com.tericcabrel.authorization.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tericcabrel.authorization.models.ConfirmAccount;

public interface ConfirmAccountRepository extends MongoRepository<ConfirmAccount, ObjectId> {
    ConfirmAccount findByToken(String token);
}
