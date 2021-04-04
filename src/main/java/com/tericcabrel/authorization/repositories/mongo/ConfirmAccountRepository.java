package com.tericcabrel.authorization.repositories.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tericcabrel.authorization.models.entities.ConfirmAccount;


@Repository
public interface ConfirmAccountRepository extends MongoRepository<ConfirmAccount, ObjectId> {
    ConfirmAccount findByToken(String token);
}
