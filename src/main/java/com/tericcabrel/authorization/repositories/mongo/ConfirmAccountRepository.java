package com.tericcabrel.authorization.repositories.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tericcabrel.authorization.models.ConfirmAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmAccountRepository extends MongoRepository<ConfirmAccount, ObjectId> {
    ConfirmAccount findByToken(String token);
}
