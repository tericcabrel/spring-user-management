package com.tericcabrel.authorization.services;

import com.tericcabrel.authorization.services.interfaces.RoleService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tericcabrel.authorization.models.dtos.CreateRoleDto;
import com.tericcabrel.authorization.models.entities.Role;
import com.tericcabrel.authorization.repositories.RoleRepository;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(CreateRoleDto createRoleDto) {
        return roleRepository.save(createRoleDto.toRole());
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
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Optional<Role> findById(String id) {
        return roleRepository.findById(new ObjectId(id));
    }

    @Override
    public Role update(String id, CreateRoleDto createRoleDto) {
        Optional<Role> role = findById(id);


        if(role.isPresent()) {
            Role roleToUpdate = role.get();

            roleToUpdate
                .setName(createRoleDto.getName())
                .setDescription(createRoleDto.getDescription());

            return roleRepository.save(roleToUpdate);
        }

        return null;
    }

    @Override
    public Role update(Role role) {
        return roleRepository.save(role);
    }
}
