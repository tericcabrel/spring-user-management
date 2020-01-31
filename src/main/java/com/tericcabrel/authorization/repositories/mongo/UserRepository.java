package com.tericcabrel.authorization.repositories.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tericcabrel.authorization.models.mongo.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByEmail(String email);
}
