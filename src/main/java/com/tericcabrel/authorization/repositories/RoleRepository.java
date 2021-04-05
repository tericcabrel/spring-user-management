package com.tericcabrel.authorization.repositories;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tericcabrel.authorization.models.entities.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Optional<Role> findByName(String name);
}
