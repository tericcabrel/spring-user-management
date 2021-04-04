package com.tericcabrel.authorization.repositories.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tericcabrel.authorization.models.entities.User;

@Repository(value = "com.tericcabrel.authorization.repositories.mongo.UserRepository")
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByEmail(String email);
}
