package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.dtos.LoginUserDto;
import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping(value = "/register")
    public User registerUser(@Valid @RequestBody UserDto userDto) {
        return null;
    }

    @PostMapping(value = "/login")
    public User loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        return null;
    }
}
