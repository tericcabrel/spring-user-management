package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.dtos.LoginUserDto;
import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class TokenController {

    @PostMapping(value = "/validate")
    public User validate(@Valid @RequestBody UserDto userDto) {
        return null;
    }

    @PostMapping(value = "/refresh")
    public User refresh(@Valid @RequestBody LoginUserDto loginUserDto) {
        return null;
    }
}
