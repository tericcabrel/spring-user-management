package com.tericcabrel.authorization.boostrap;

import java.util.Map;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.tericcabrel.authorization.utils.Constants.ROLE_ADMIN;
import static com.tericcabrel.authorization.utils.Constants.ROLE_SUPER_ADMIN;
import static com.tericcabrel.authorization.utils.Constants.ROLE_USER;

import com.tericcabrel.authorization.models.dtos.CreateRoleDto;
import com.tericcabrel.authorization.models.dtos.CreateUserDto;
import com.tericcabrel.authorization.models.entities.Role;
import com.tericcabrel.authorization.models.entities.User;
import com.tericcabrel.authorization.services.interfaces.RoleService;
import com.tericcabrel.authorization.services.interfaces.UserService;

@Component
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleService roleService;

    private final UserService userService;

    public DataSeeder(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();

        this.loadUsers();
    }

    private void loadRoles() {
        Map<String, String> rolesMap = new HashMap<>();
        rolesMap.put(ROLE_USER, "User role");
        rolesMap.put(ROLE_ADMIN, "Admin role");
        rolesMap.put(ROLE_SUPER_ADMIN, "Super admin role");

        rolesMap.forEach((key, value) -> {
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
