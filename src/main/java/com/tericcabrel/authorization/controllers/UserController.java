package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.models.common.ApiResponse;
import com.tericcabrel.authorization.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ApiResponse<List<User>> all(){
        return new ApiResponse<List<User>>(HttpStatus.OK.value(), userService.findAll());
    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ApiResponse<User> one(@PathVariable String id){
        return new ApiResponse<User>(HttpStatus.OK.value(), userService.findById(id));
    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable String id, @RequestBody UserDto userDto) {
        return new ApiResponse<User>(HttpStatus.OK.value(), userService.update(id, userDto));
    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}/password")
    public ApiResponse<User> updatePassword(@PathVariable String  id, @RequestBody UserDto userDto) {
        return new ApiResponse<User>(HttpStatus.OK.value(), userService.update(id, userDto));
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        userService.delete(id);

        return new ApiResponse<>(HttpStatus.OK.value(),null);
    }
}
