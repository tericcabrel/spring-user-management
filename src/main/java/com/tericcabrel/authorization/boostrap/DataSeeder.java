package com.tericcabrel.authorization.boostrap;

import com.tericcabrel.authorization.exceptions.ResourceNotFoundException;
import com.tericcabrel.authorization.services.interfaces.PermissionLoader;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.tericcabrel.authorization.utils.Constants.ROLE_ADMIN;
import static com.tericcabrel.authorization.utils.Constants.ROLE_SUPER_ADMIN;
import static com.tericcabrel.authorization.utils.Constants.ROLE_USER;

import com.tericcabrel.authorization.models.dtos.CreateRoleDto;
import com.tericcabrel.authorization.models.dtos.CreateUserDto;
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

    @SneakyThrows
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
            try {
                roleService.findByName(key);
            } catch (ResourceNotFoundException e) {
                CreateRoleDto createRoleDto = new CreateRoleDto();

                createRoleDto.setName(key)
                    .setDescription(value)
                    .setDefault(true);

                roleService.save(createRoleDto);
            }
        });
    }

    private void loadUsers() throws ResourceNotFoundException {
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

        try {
            userService.findByEmail(superAdmin.getEmail());
        } catch (ResourceNotFoundException e) {
            superAdmin.setRole(roleService.findByName(ROLE_SUPER_ADMIN));

            userService.save(superAdmin);
        }
    }
}
