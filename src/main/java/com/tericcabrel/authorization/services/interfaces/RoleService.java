package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.models.dtos.CreateRoleDto;
import com.tericcabrel.authorization.models.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role save(CreateRoleDto role);

    List<Role> findAll();

    void delete(String id);

    Optional<Role> findByName(String name);

    Role findById(String id);

    Role update(String id, CreateRoleDto createRoleDto);
}
