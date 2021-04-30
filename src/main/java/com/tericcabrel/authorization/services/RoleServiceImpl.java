package com.tericcabrel.authorization.services;

import static com.tericcabrel.authorization.utils.Constants.ROLE_NOT_FOUND_MESSAGE;

import com.tericcabrel.authorization.exceptions.ResourceNotFoundException;
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
    public Role findByName(String name) throws ResourceNotFoundException {
        Optional<Role> roleOptional = roleRepository.findByName(name);

        if (roleOptional.isEmpty()) {
            throw new ResourceNotFoundException(ROLE_NOT_FOUND_MESSAGE);
        }

        return roleOptional.get();
    }

    @Override
    public Role findById(String id) throws ResourceNotFoundException {
        Optional<Role> roleOptional = roleRepository.findById(new ObjectId(id));

        if (roleOptional.isEmpty()) {
            throw new ResourceNotFoundException(ROLE_NOT_FOUND_MESSAGE);
        }

        return roleOptional.get();
    }

    @Override
    public Role update(String id, CreateRoleDto createRoleDto) throws ResourceNotFoundException {
        Role roleToUpdate = findById(id);

        roleToUpdate
            .setName(createRoleDto.getName())
            .setDescription(createRoleDto.getDescription());

        return roleRepository.save(roleToUpdate);
    }

    @Override
    public Role update(Role role) {
        return roleRepository.save(role);
    }
}
