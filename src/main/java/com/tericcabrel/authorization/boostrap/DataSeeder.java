package com.tericcabrel.authorization.boostrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.tericcabrel.authorization.utils.Constants.ROLE_ADMIN;
import static com.tericcabrel.authorization.utils.Constants.ROLE_USER;

import com.tericcabrel.authorization.models.dto.RoleDto;
import com.tericcabrel.authorization.models.dto.UserDto;
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
                RoleDto roleDto = new RoleDto();

                roleDto.setName(key)
                    .setDescription(value);

                roleService.save(roleDto);
            }
        });
    }

    private void loadUsers() {
        Set<UserDto> users = new HashSet<UserDto>() {};

        UserDto admin = new UserDto()
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

        users.forEach((userDto) -> {
            User obj = userService.findByEmail(userDto.getEmail());
            Role role;

            if (obj == null ){
                role = roleService.findByName(ROLE_ADMIN);

                Set<Role> userRoles = new HashSet<>();
                userRoles.add(role);

                userDto.setRoles(userRoles);

                userService.save(userDto);
            }
        });
    }
}
