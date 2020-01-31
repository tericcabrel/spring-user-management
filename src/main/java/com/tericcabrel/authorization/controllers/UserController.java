package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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
import java.io.IOException;

import org.hibernate.validator.constraints.Length;

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
    private IUserService userService;

    private FileStorageService fileStorageService;

    public UserController(IUserService userService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @ApiOperation(value = "Get all users", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List retrieved successfully!", response = UserListResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = INVALID_DATA_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ServiceResponse> all(){
        return ResponseEntity.ok(
                new ServiceResponse(HttpStatus.OK.value(), userService.findAll())
        );
    }

    @ApiOperation(value = "Get the authenticated user", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User retrieved successfully!", response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<ServiceResponse> currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                new ServiceResponse(HttpStatus.OK.value(), userService.findByEmail(authentication.getName()))
        );
    }

    @ApiOperation(value = "Get one user", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Item retrieved successfully!", response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> one(@PathVariable String id){
        return ResponseEntity.ok(
            new ServiceResponse(HttpStatus.OK.value(), userService.findById(id))
        );
    }

    @ApiOperation(value = "Update an user", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User updated successfully!", response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(@PathVariable String id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(
            new ServiceResponse(HttpStatus.OK.value(), userService.update(id, updateUserDto))
        );
    }

    @ApiOperation(value = "Update user password", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The password updated successfully!", response = UserResponse.class),
        @ApiResponse(code = 400, message = "The current password is invalid", response = BadRequestResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}/password")
    public ResponseEntity<ServiceResponse> updatePassword(
            @PathVariable String id, @Valid @RequestBody UpdatePasswordDto updatePasswordDto
    ) throws PasswordNotMatchException {
        User user = userService.updatePassword(id, updatePasswordDto);

        if (user == null) {
            throw new PasswordNotMatchException("The current password don't match!");
        }

        return ResponseEntity.ok(new ServiceResponse(HttpStatus.OK.value(), user));
    }

    @ApiOperation(value = "Delete an user", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "User deleted successfully!", response = SuccessResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Change or delete user picture", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The picture updated/deleted successfully!", response = SuccessResponse.class),
        @ApiResponse(code = 400, message = "An IOException occurred!", response = BadRequestResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PostMapping("/{id}/picture")
    public ResponseEntity<ServiceResponse> uploadPicture(
        @PathVariable String id,
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam("action")
        @Pattern(regexp = "[ud]", message = "The valid value can be \"u\" or \"d\"")
        @Length(max = 1, message = "This field length can\'t be greater than 1")
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
                Resource resource = fileStorageService.loadFileAsResource(user.getAvatar());

                boolean deleted = resource.getFile().delete();

                if (deleted) {
                    user.setAvatar(null);
                    userService.update(user);
                }
            }
        } else {
            System.out.println("Unknown action!");
        }

        return ResponseEntity.ok().body(new ServiceResponse(HttpStatus.OK.value(), user));
    }
}
