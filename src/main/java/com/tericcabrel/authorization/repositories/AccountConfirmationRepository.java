package com.tericcabrel.authorization.repositories;

import com.tericcabrel.authorization.models.AccountConfirmation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountConfirmationRepository extends MongoRepository<AccountConfirmation, ObjectId> {
    AccountConfirmation findByToken(String token);
}
