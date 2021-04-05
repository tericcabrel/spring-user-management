package com.tericcabrel.authorization.boostrap;

import com.tericcabrel.authorization.services.interfaces.PermissionLoader;
import java.util.Map;
import java.util.Optional;
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

    private final PermissionLoader permissionLoader;

    public DataSeeder(RoleService roleService, UserService userService, PermissionLoader permissionLoader) {
        this.roleService = roleService;
        this.userService = userService;
        this.permissionLoader = permissionLoader;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadRoles();

        permissionLoader.load();

        loadUsers();
    }

    private void loadRoles() {
        Map<String, String> rolesMap = new HashMap<>();
        rolesMap.put(ROLE_USER, "User role");
        rolesMap.put(ROLE_ADMIN, "Admin role");
        rolesMap.put(ROLE_SUPER_ADMIN, "Super admin role");

        rolesMap.forEach((key, value) -> {
            Optional<Role> role = roleService.findByName(key);

            role.ifPresent(roleFound -> {
                CreateRoleDto createRoleDto = new CreateRoleDto();

                createRoleDto.setName(key)
                    .setDescription(value)
                    .setDefault(true);

                roleService.save(createRoleDto);
            });
        });
    }

    private void loadUsers() {
        Set<CreateUserDto> users = new HashSet<>() {};

        CreateUserDto superAdmin = new CreateUserDto()
                .setEmail("sadmin@authoz.com")
                .setFirstName("Super")
                .setLastName("Admin")
                .setConfirmed(true)
                .setEnabled(true)
                .setAvatar(null)
                .setGender("M")
                .setTimezone("Europe/Paris")
                .setCoordinates(null)
                .setPassword("secret");

        users.add(superAdmin);

        users.forEach(createUserDto -> {
            User obj = userService.findByEmail(createUserDto.getEmail());

            if (obj == null ){
                Optional<Role> role = roleService.findByName(ROLE_SUPER_ADMIN);

                role.ifPresent(roleFound -> {
                    Set<Role> userRoles = new HashSet<>();
                    userRoles.add(role.get());

                    createUserDto.setRoles(userRoles);

                    userService.save(createUserDto);
                });
            }
        });
    }
}
