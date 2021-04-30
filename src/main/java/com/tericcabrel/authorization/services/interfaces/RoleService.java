package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.exceptions.ResourceNotFoundException;
import com.tericcabrel.authorization.models.dtos.CreateRoleDto;
import com.tericcabrel.authorization.models.entities.Role;

import java.util.List;

public interface RoleService {
    Role save(CreateRoleDto role);

    List<Role> findAll();

    void delete(String id);

    Role findByName(String name) throws ResourceNotFoundException;

    Role findById(String id) throws ResourceNotFoundException;

    Role update(String id, CreateRoleDto createRoleDto) throws ResourceNotFoundException;
    Role update(Role role);
}
