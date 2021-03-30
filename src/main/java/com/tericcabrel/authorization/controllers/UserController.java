package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import java.nio.file.Files;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;

import static com.tericcabrel.authorization.utils.Constants.*;

import com.tericcabrel.authorization.models.response.*;
import com.tericcabrel.authorization.models.dto.UpdatePasswordDto;
import com.tericcabrel.authorization.models.dto.UpdateUserDto;
import com.tericcabrel.authorization.models.mongo.User;
import com.tericcabrel.authorization.exceptions.PasswordNotMatchException;
import com.tericcabrel.authorization.services.FileStorageService;
import com.tericcabrel.authorization.services.interfaces.IUserService;


@Api(tags = "User management", description = "Operations pertaining to user's update, fetch and delete")
@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {
    private final Log logger = LogFactory.getLog(this.getClass());

    private final IUserService userService;

    private final FileStorageService fileStorageService;

    public UserController(IUserService userService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @ApiOperation(value = "Get all users", response = GenericResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "List retrieved successfully!", response = UserListResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = INVALID_DATA_MESSAGE, response = GenericResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> all(){
        return ResponseEntity.ok(userService.findAll());
    }

    @ApiOperation(value = "Get the authenticated user", response = GenericResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "User retrieved successfully!", response = UserResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = GenericResponse.class),
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<User> currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(userService.findByEmail(authentication.getName()));
    }

    @ApiOperation(value = "Get one user", response = GenericResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item retrieved successfully!", response = UserResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = GenericResponse.class),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> one(@PathVariable String id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @ApiOperation(value = "Update an user", response = GenericResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "User updated successfully!", response = UserResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable String id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.update(id, updateUserDto));
    }

    @ApiOperation(value = "Update user password", response = GenericResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "The password updated successfully!", response = UserResponse.class),
        @io.swagger.annotations.ApiResponse(code = 400, message = "The current password is invalid", response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}/password")
    public ResponseEntity<User> updatePassword(
            @PathVariable String id, @Valid @RequestBody UpdatePasswordDto updatePasswordDto
    ) throws PasswordNotMatchException {
        User user = userService.updatePassword(id, updatePasswordDto);

        if (user == null) {
            throw new PasswordNotMatchException("The current password don't match!");
        }

        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Delete a user", response = GenericResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 204, message = "User deleted successfully!", response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = GenericResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Change or delete user picture", response = GenericResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "The picture updated/deleted successfully!", response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 400, message = "An IOException occurred!", response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = GenericResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping("/{id}/picture")
    public ResponseEntity<User> uploadPicture(
        @PathVariable String id,
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam("action")
        @Pattern(regexp = "[ud]", message = "The valid value can be \"u\" or \"d\"")
        @Size(max = 1, message = "This field length can't be greater than 1")
        @NotBlank(message = "This field is required")
                    String action
    ) throws IOException {
        User user = null;
        UpdateUserDto updateUserDto = new UpdateUserDto();

        if (action.equals("u")) {
            String fileName = fileStorageService.storeFile(file);

            updateUserDto.setAvatar(fileName);

            user = userService.update(id, updateUserDto);
        } else if (action.equals("d")) {
            user = userService.findById(id);

            if (user.getAvatar() != null) {
                boolean deleted = fileStorageService.deleteFile(user.getAvatar());

                if (deleted) {
                    user.setAvatar(null);
                    userService.update(user);
                }
            }
        } else {
            logger.info("Unknown action!");
        }

        return ResponseEntity.ok().body(user);
    }
}
