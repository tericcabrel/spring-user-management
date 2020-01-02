package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.dtos.LoginUserDto;
import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.services.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public User registerUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.save(userDto);

        return user;
    }

    @PostMapping(value = "/login")
    public User loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        return null;
    }
}
