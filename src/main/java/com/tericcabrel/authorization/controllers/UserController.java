package com.tericcabrel.authorization.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import com.tericcabrel.authorization.models.common.ApiResponse;
import com.tericcabrel.authorization.services.interfaces.UserService;
import com.tericcabrel.authorization.dtos.UpdatePasswordDto;
import com.tericcabrel.authorization.dtos.UpdateUserDto;
import com.tericcabrel.authorization.exceptions.PasswordNotMatchException;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.services.FileStorageService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    FileStorageService fileStorageService;

    public UserController(UserService userService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
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
    public ResponseEntity<ApiResponse> update(@PathVariable String id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), userService.update(id, updateUserDto))
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updatePassword(
            @PathVariable String id, @Valid @RequestBody UpdatePasswordDto updatePasswordDto
    ) throws PasswordNotMatchException {
        User user = userService.updatePassword(id, updatePasswordDto);

        if (user == null) {
            throw new PasswordNotMatchException("The current password don't match!");
        }

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), user));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/picture")
    public ResponseEntity<ApiResponse> uploadPicture(
            @PathVariable String id, @RequestParam("file") MultipartFile file, @RequestParam("action") String action
    ) throws IOException {
        User user;
        UpdateUserDto updateUserDto = new UpdateUserDto();

        if (action.equals("u")) {
            String fileName = fileStorageService.storeFile(file);

            updateUserDto.setAvatar(fileName);

            user = userService.update(id, updateUserDto);
        } else {
            user = userService.findById(id);

            Resource resource = fileStorageService.loadFileAsResource(user.getAvatar());

            boolean deleted = resource.getFile().delete();

            if (deleted) {
                user.setAvatar(null);
                userService.update(user);
            }
        }

        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), user));
    }
}
