package com.tericcabrel.authorization.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tericcabrel.authorization.models.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByEmail(String email);
}
