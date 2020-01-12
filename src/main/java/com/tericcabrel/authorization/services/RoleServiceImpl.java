package com.tericcabrel.authorization.services;

import com.tericcabrel.authorization.dtos.RoleDto;
import com.tericcabrel.authorization.models.Role;
import com.tericcabrel.authorization.repositories.RoleRepository;
import com.tericcabrel.authorization.services.interfaces.RoleService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(RoleDto roleDto) {
        Role newRole = new Role();
        Date dateNow = new Date();

        newRole.setName(roleDto.getName())
                .setDescription(roleDto.getDescription())
                .setCreatedAt(dateNow)
                .setUpdatedAt(dateNow);

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
    public Role update(String id, RoleDto roleDto) {
        Role role = findById(id);

        if(role != null) {
            role.setName(roleDto.getName());
            role.setDescription(roleDto.getDescription());

            roleRepository.save(role);

            return role;
        }

        return null;
    }
}
