package com.tericcabrel.authorization.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tericcabrel.authorization.models.Role;

public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Role findByName(String name);
}
