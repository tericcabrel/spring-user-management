package com.tericcabrel.authorization.repositories;

import com.tericcabrel.authorization.models.entities.Permission;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, ObjectId> {
    Permission findByName(String name);
}
