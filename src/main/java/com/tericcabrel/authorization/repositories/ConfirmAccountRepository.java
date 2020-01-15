package com.tericcabrel.authorization.repositories;

import com.tericcabrel.authorization.models.ConfirmAccount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfirmAccountRepository extends MongoRepository<ConfirmAccount, ObjectId> {
    ConfirmAccount findByToken(String token);
}
