package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.dtos.LoginUserDto;
import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.Role;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.services.interfaces.RoleService;
import com.tericcabrel.authorization.services.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    private RoleService roleService;

    public AuthController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping(value = "/register")
    public User registerUser(@Valid @RequestBody UserDto userDto) {
        Role role = roleService.findById("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userDto.setRoles(roles);

        User user = userService.save(userDto);

        return user;
    }

    @PostMapping(value = "/login")
    public User loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        return null;
    }
}
