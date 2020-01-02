package com.tericcabrel.authorization.boostrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.tericcabrel.authorization.models.Role;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.repositories.RoleRepository;
import com.tericcabrel.authorization.repositories.UserRepository;

@Component
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private RoleRepository roleRepository;

    private UserRepository userRepository;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();

        this.loadUsers();
    }

    private void loadRoles() {
        HashMap<String, String> roles = new HashMap<>();
        roles.put("ROLE_USER", "User role");
        roles.put("ROLE_ADMIN", "Admin role");

        roles.forEach((key, value) -> {
            Role role = roleRepository.findByName(key);

            if (role == null) {
                role = new Role();
                Date dateNow = new Date();

                role.setName(key)
                    .setDescription(value)
                    .setCreatedAt(dateNow)
                    .setUpdatedAt(dateNow);

                roleRepository.save(role);
            }
        });
    }

    private void loadUsers() {
        Set<User> users = new HashSet<User>() {};

        User admin = new User()
                .setEmail("admin@admin.com")
                .setFirstName("Admin")
                .setLastName("User")
                .setConfirmed(true)
                .setEnabled(true)
                .setAvatar(null)
                .setGender("M")
                .setTimezone("Africa/Douala")
                .setCoordinates(null)
                .setPassword("$2a$10$4NHGIKeU5EPSicQdEL3O7uuJoA.D6otB74g38XXeTw59xXOBHcLU2");

        users.add(admin);

        users.forEach((u) -> {
            User obj = userRepository.findByEmail(u.getEmail());
            Role role;

            if (obj == null ){
                role = roleRepository.findByName("ROLE_ADMIN");

                u.getRoles().add(role);

                Date dateNow = new Date();

                u.setCreatedAt(dateNow)
                 .setUpdatedAt(dateNow);

                userRepository.save(u);
            }
        });
    }
}
