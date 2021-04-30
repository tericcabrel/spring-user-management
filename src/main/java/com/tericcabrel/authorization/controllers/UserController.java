package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.exceptions.ResourceNotFoundException;
import com.tericcabrel.authorization.models.dtos.UpdateUserPermissionDto;
import com.tericcabrel.authorization.models.entities.Permission;
import com.tericcabrel.authorization.services.interfaces.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.Optional;
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
import com.tericcabrel.authorization.models.dtos.UpdatePasswordDto;
import com.tericcabrel.authorization.models.dtos.UpdateUserDto;
import com.tericcabrel.authorization.models.entities.User;
import com.tericcabrel.authorization.exceptions.PasswordNotMatchException;
import com.tericcabrel.authorization.services.FileStorageServiceImpl;
import com.tericcabrel.authorization.services.interfaces.UserService;


@Api(tags = SWG_USER_TAG_NAME, description = SWG_USER_TAG_DESCRIPTION)
@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {
    private final Log logger = LogFactory.getLog(this.getClass());

    private final UserService userService;

    private final PermissionService permissionService;

    private final FileStorageServiceImpl fileStorageServiceImpl;

    public UserController(UserService userService, PermissionService permissionService, FileStorageServiceImpl fileStorageServiceImpl) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.fileStorageServiceImpl = fileStorageServiceImpl;
    }

    @ApiOperation(value = SWG_USER_LIST_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_USER_LIST_MESSAGE, response = UserListResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = INVALID_DATA_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasAuthority('read:users')")
    @GetMapping
    public ResponseEntity<UserListResponse> all(){
        return ResponseEntity.ok(new UserListResponse(userService.findAll()));
    }

    @ApiOperation(value = SWG_USER_LOGGED_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_USER_LOGGED_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> currentUser() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(new UserResponse(userService.findByEmail(authentication.getName())));
    }

    @ApiOperation(value = SWG_USER_ITEM_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_USER_ITEM_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasAuthority('read:user')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> one(@PathVariable String id)
        throws ResourceNotFoundException {
        return ResponseEntity.ok(new UserResponse(userService.findById(id)));
    }

    @ApiOperation(value = SWG_USER_UPDATE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_USER_UPDATE_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('update:user')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id, @RequestBody UpdateUserDto updateUserDto)
        throws ResourceNotFoundException {
        return ResponseEntity.ok(new UserResponse(userService.update(id, updateUserDto)));
    }

    @ApiOperation(value = SWG_USER_UPDATE_PWD_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_USER_UPDATE_PWD_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 400, message = SWG_USER_UPDATE_PWD_ERROR, response = BadRequestResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('change:password')")
    @PutMapping("/{id}/password")
    public ResponseEntity<UserResponse> updatePassword(
            @PathVariable String id, @Valid @RequestBody UpdatePasswordDto updatePasswordDto
    ) throws PasswordNotMatchException, ResourceNotFoundException {
        User user = userService.updatePassword(id, updatePasswordDto);

        if (user == null) {
            throw new PasswordNotMatchException(PASSWORD_NOT_MATCH_MESSAGE);
        }

        return ResponseEntity.ok(new UserResponse(user));
    }

    @ApiOperation(value = SWG_USER_DELETE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = SWG_USER_DELETE_MESSAGE, response = SuccessResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = SuccessResponse.class),
    })
    @PreAuthorize("hasAuthority('delete:user')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = SWG_USER_PICTURE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_USER_PICTURE_MESSAGE, response = SuccessResponse.class),
        @ApiResponse(code = 400, message = SWG_USER_PICTURE_ERROR, response = SuccessResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = SuccessResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = SuccessResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('change:picture')")
    @PostMapping("/{id}/picture")
    public ResponseEntity<UserResponse> uploadPicture(
        @PathVariable String id,
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam("action")
        @Pattern(regexp = "[ud]", message = "The valid value can be \"u\" or \"d\"")
        @Size(max = 1, message = "This field length can't be greater than 1")
        @NotBlank(message = "This field is required")
                    String action
    ) throws IOException, ResourceNotFoundException {
        User user = null;
        UpdateUserDto updateUserDto = new UpdateUserDto();

        if (action.equals("u")) {
            String fileName = fileStorageServiceImpl.storeFile(file);

            updateUserDto.setAvatar(fileName);

            user = userService.update(id, updateUserDto);
        } else if (action.equals("d")) {
            user = userService.findById(id);

            if (user.getAvatar() != null) {
                boolean deleted = fileStorageServiceImpl.deleteFile(user.getAvatar());

                if (deleted) {
                    user.setAvatar(null);
                    userService.update(user);
                }
            }
        } else {
            logger.info(USER_PICTURE_NO_ACTION_MESSAGE);
        }

        return ResponseEntity.ok().body(new UserResponse(user));
    }


    @ApiOperation(value = SWG_USER_PERMISSION_ASSIGN_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_USER_PERMISSION_ASSIGN_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('assign:permission')")
    @PutMapping("/{id}/permissions")
    public ResponseEntity<UserResponse> assignPermissions(@PathVariable String id, @Valid @RequestBody UpdateUserPermissionDto updateUserPermissionDto)
        throws ResourceNotFoundException {
        User user = userService.findById(id);

        Arrays.stream(updateUserPermissionDto.getPermissions()).forEach(permissionName -> {
            Optional<Permission> permission = permissionService.findByName(permissionName);

            if (permission.isPresent() && !user.hasPermission(permissionName)) {
                user.addPermission(permission.get());
            }
        });

        userService.update(user);

        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @ApiOperation(value = SWG_USER_PERMISSION_REVOKE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_USER_PERMISSION_REVOKE_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('revoke:permission')")
    @DeleteMapping("/{id}/permissions")
    public ResponseEntity<User> revokePermissions(@PathVariable String id, @Valid @RequestBody UpdateUserPermissionDto updateUserPermissionDto)
        throws ResourceNotFoundException {
        User user = userService.findById(id);

        Arrays.stream(updateUserPermissionDto.getPermissions()).forEach(permissionName -> {
            Optional<Permission> permission = permissionService.findByName(permissionName);

            if (permission.isPresent() && user.hasPermission(permissionName)) {
                user.removePermission(permission.get());
            }
        });

        userService.update(user);

        return ResponseEntity.ok().body(user);
    }
}
