package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

import static com.tericcabrel.authorization.utils.Constants.*;

import com.tericcabrel.authorization.models.response.*;
import com.tericcabrel.authorization.models.dto.CreateRoleDto;
import com.tericcabrel.authorization.models.dto.UpdateRoleDto;
import com.tericcabrel.authorization.models.mongo.Role;
import com.tericcabrel.authorization.models.mongo.User;
import com.tericcabrel.authorization.services.interfaces.IRoleService;
import com.tericcabrel.authorization.services.interfaces.IUserService;


@Api(tags = "Role management", description = "Operations pertaining to role creation, update, assign, revoke, fetch and delete")
@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    private final IRoleService roleService;

    private final IUserService userService;

    public RoleController(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ApiOperation(value = "Create a role", response = SuccessResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Role created successfully!", response = Role.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Role> create(@Valid @RequestBody CreateRoleDto createRoleDto){
        Role role = roleService.save(createRoleDto);

        return ResponseEntity.ok(role);
    }

    @ApiOperation(value = "Get all roles", response = SuccessResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "List retrieved successfully!", response = RoleListResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<RoleListResponse> all(){
        return ResponseEntity.ok(new RoleListResponse(roleService.findAll()));
    }

    @ApiOperation(value = "Get one role", response = SuccessResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item retrieved successfully!", response = RoleResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> one(@PathVariable String id){
        return ResponseEntity.ok(new RoleResponse(roleService.findById(id)));
    }

    @ApiOperation(value = "Update a role", response = SuccessResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Role updated successfully!", response = RoleResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable String id, @Valid @RequestBody CreateRoleDto createRoleDto) {
        return ResponseEntity.ok(new RoleResponse(roleService.update(id, createRoleDto)));
    }

    @ApiOperation(value = "Delete a role", response = SuccessResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 204, message = "Role deleted successfully!", response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        roleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Assign roles to an user", response = SuccessResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Roles successfully assigned to user!", response = UserResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/assign")
    public ResponseEntity<UserResponse> assignRoles(@Valid @RequestBody UpdateRoleDto updateRoleDto) {
        User user = userService.findById(updateRoleDto.getUserId());

        Arrays.stream(updateRoleDto.getRoles()).forEach(role -> {
            Role roleObject = roleService.findByName(role);

            if (roleObject != null && !user.hasRole(role)) {
                user.addRole(roleObject);
            }
        });

        userService.update(user);

        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @ApiOperation(value = "Assign roles to an user", response = SuccessResponse.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Roles successfully assigned to user!", response = UserResponse.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @io.swagger.annotations.ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/revoke")
    public ResponseEntity<User> revokeRoles(@Valid @RequestBody UpdateRoleDto updateRoleDto) {
        User user = userService.findById(updateRoleDto.getUserId());

        Arrays.stream(updateRoleDto.getRoles()).forEach(role -> {
            Role roleObject = roleService.findByName(role);

            if (roleObject != null && user.hasRole(role)) {
                user.removeRole(roleObject);
            }
        });

        userService.update(user);

        return ResponseEntity.ok().body(user);
    }
}
