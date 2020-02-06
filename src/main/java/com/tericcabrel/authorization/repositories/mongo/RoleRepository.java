package com.tericcabrel.authorization.repositories.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tericcabrel.authorization.models.mongo.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Role findByName(String name);
}
