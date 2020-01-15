package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.dtos.RoleDto;
import com.tericcabrel.authorization.models.Role;

import java.util.List;

public interface IRoleService {
    Role save(RoleDto role);

    List<Role> findAll();

    void delete(String id);

    Role findByName(String name);

    Role findById(String id);

    Role update(String id, RoleDto roleDto);
}
