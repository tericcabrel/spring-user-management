package com.tericcabrel.authorization.repositories;

import com.tericcabrel.authorization.models.Role;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Role findByName(String name);
}
