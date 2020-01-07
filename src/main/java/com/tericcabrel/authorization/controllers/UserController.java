package com.tericcabrel.authorization.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.common.ApiResponse;
import com.tericcabrel.authorization.services.interfaces.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> all(){
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), userService.findAll())
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), userService.findByEmail(authentication.getName()))
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> one(@PathVariable String id){
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), userService.findById(id))
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable String id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), userService.update(id, userDto))
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updatePassword(@PathVariable String  id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), userService.update(id, userDto))
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
