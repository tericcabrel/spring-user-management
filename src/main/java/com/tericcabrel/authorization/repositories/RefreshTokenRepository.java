package com.tericcabrel.authorization.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tericcabrel.authorization.models.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    RefreshToken findByValue(String value);
}