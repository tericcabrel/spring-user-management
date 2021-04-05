package com.tericcabrel.authorization.services;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tericcabrel.authorization.models.dtos.CreateRoleDto;
import com.tericcabrel.authorization.models.entities.Role;
import com.tericcabrel.authorization.repositories.mongo.RoleRepository;

@Service(value = "roleService")
public class RoleServiceImpl implements com.tericcabrel.authorization.services.interfaces.RoleService {
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(CreateRoleDto createRoleDto) {
        Role newRole = new Role();

        newRole.setName(createRoleDto.getName())
               .setDescription(createRoleDto.getDescription());

        return roleRepository.save(newRole);
    }

    @Override
    public List<Role> findAll() {
        List<Role> list = new ArrayList<>();
        roleRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public void delete(String id) {
        roleRepository.deleteById(new ObjectId(id));
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role findById(String id) {
        Optional<Role> optionalRole = roleRepository.findById(new ObjectId(id));

        return optionalRole.orElse(null);
    }

    @Override
    public Role update(String id, CreateRoleDto createRoleDto) {
        Role role = findById(id);

        if(role != null) {
            role.setName(createRoleDto.getName());
            role.setDescription(createRoleDto.getDescription());

            roleRepository.save(role);

            return role;
        }

        return null;
    }
}
