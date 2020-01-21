package com.tericcabrel.authorization.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

import com.tericcabrel.authorization.dtos.RoleDto;
import com.tericcabrel.authorization.dtos.RoleUpdateDto;
import com.tericcabrel.authorization.models.Role;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.models.common.ServiceResponse;
import com.tericcabrel.authorization.services.interfaces.IRoleService;
import com.tericcabrel.authorization.services.interfaces.IUserService;

@Api(tags = "Role management", description = "Operations pertaining to role creation, update, assign, revoke, fetch and delete")
@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    private IRoleService roleService;

    private IUserService userService;

    public RoleController(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ApiOperation(value = "Create a role", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Role created successfully!"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "You don't have the right to access to this resource"),
        @ApiResponse(code = 422, message = "One or many parameters in the request's body are invalid"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceResponse> create(@Valid @RequestBody RoleDto roleDto){
        Role role = roleService.save(roleDto);

        return ResponseEntity.ok(new ServiceResponse(HttpStatus.OK.value(), role));
    }

    @ApiOperation(value = "Get all roles", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List retrieved successfully!"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "You don't have the right to access to this resource"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ServiceResponse> all(){
        return ResponseEntity.ok(new ServiceResponse(HttpStatus.OK.value(), roleService.findAll()));
    }

    @ApiOperation(value = "Get one role", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Item retrieved successfully!"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "You don't have the right to access to this resource"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> one(@PathVariable String id){
        return ResponseEntity.ok(new ServiceResponse(HttpStatus.OK.value(), roleService.findById(id)));
    }

    @ApiOperation(value = "Update a role", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Role updated successfully!"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "You don't have the right to access to this resource"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(@PathVariable String id, @RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(new ServiceResponse(HttpStatus.OK.value(), roleService.update(id, roleDto)));
    }

    @ApiOperation(value = "Delete a role", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Role deleted successfully!"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "You don't have the right to access to this resource"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        roleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Assign roles to an user", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Roles successfully assigned to user!"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "You don't have the right to access to this resource"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/assign")
    public ResponseEntity assignRoles(@Valid @RequestBody RoleUpdateDto roleUpdateDto) {
        User user = userService.findById(roleUpdateDto.getUserId());

        Arrays.stream(roleUpdateDto.getRoles()).forEach(role -> {
            Role roleObject = roleService.findByName(role);

            if (roleObject != null && !user.hasRole(role)) {
                user.addRole(roleObject);
            }
        });

        userService.update(user);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Assign roles to an user", response = ServiceResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Roles successfully assigned to user!"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "You don't have the right to access to this resource"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/revoke")
    public ResponseEntity revokeRoles(@Valid @RequestBody RoleUpdateDto roleUpdateDto) {
        User user = userService.findById(roleUpdateDto.getUserId());

        Arrays.stream(roleUpdateDto.getRoles()).forEach(role -> {
            Role roleObject = roleService.findByName(role);

            if (roleObject != null && user.hasRole(role)) {
                user.removeRole(roleObject);
            }
        });

        userService.update(user);

        return ResponseEntity.ok().body(user);
    }
}
