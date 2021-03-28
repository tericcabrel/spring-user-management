package com.tericcabrel.authorization.boostrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.tericcabrel.authorization.utils.Constants.ROLE_ADMIN;
import static com.tericcabrel.authorization.utils.Constants.ROLE_USER;

import com.tericcabrel.authorization.models.dto.CreateRoleDto;
import com.tericcabrel.authorization.models.dto.CreateUserDto;
import com.tericcabrel.authorization.models.mongo.Role;
import com.tericcabrel.authorization.models.mongo.User;
import com.tericcabrel.authorization.services.interfaces.IRoleService;
import com.tericcabrel.authorization.services.interfaces.IUserService;

@Component
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private IRoleService roleService;

    private IUserService userService;

    public DataSeeder(IRoleService roleService, IUserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();

        this.loadUsers();
    }

    private void loadRoles() {
        HashMap<String, String> roles = new HashMap<>();
        roles.put(ROLE_USER, "User role");
        roles.put(ROLE_ADMIN, "Admin role");

        roles.forEach((key, value) -> {
            Role role = roleService.findByName(key);

            if (role == null) {
                CreateRoleDto createRoleDto = new CreateRoleDto();

                createRoleDto.setName(key)
                    .setDescription(value);

                roleService.save(createRoleDto);
            }
        });
    }

    private void loadUsers() {
        Set<CreateUserDto> users = new HashSet<CreateUserDto>() {};

        CreateUserDto admin = new CreateUserDto()
                .setEmail("admin@admin.com")
                .setFirstName("Admin")
                .setLastName("User")
                .setConfirmed(true)
                .setEnabled(true)
                .setAvatar(null)
                .setGender("M")
                .setTimezone("Africa/Douala")
                .setCoordinates(null)
                .setPassword("secret");

        users.add(admin);

        users.forEach(createUserDto -> {
            User obj = userService.findByEmail(createUserDto.getEmail());
            Role role;

            if (obj == null ){
                role = roleService.findByName(ROLE_ADMIN);

                Set<Role> userRoles = new HashSet<>();
                userRoles.add(role);

                createUserDto.setRoles(userRoles);

                userService.save(createUserDto);
            }
        });
    }
}
