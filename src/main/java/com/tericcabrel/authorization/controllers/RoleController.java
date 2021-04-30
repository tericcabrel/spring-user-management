package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.exceptions.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

import static com.tericcabrel.authorization.utils.Constants.*;

import com.tericcabrel.authorization.models.response.*;
import com.tericcabrel.authorization.models.dtos.CreateRoleDto;
import com.tericcabrel.authorization.models.entities.Role;
import com.tericcabrel.authorization.services.interfaces.RoleService;
import com.tericcabrel.authorization.models.dtos.UpdateRolePermissionDto;
import com.tericcabrel.authorization.models.entities.Permission;
import com.tericcabrel.authorization.services.interfaces.PermissionService;

@Api(tags = SWG_ROLE_TAG_NAME, description = SWG_ROLE_TAG_DESCRIPTION)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    private final RoleService roleService;

    private final PermissionService permissionService;

    public RoleController(PermissionService permissionService, RoleService roleService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @ApiOperation(value = SWG_ROLE_CREATE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_CREATE_MESSAGE, response = Role.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('create:role')")
    @PostMapping
    public ResponseEntity<Role> create(@Valid @RequestBody CreateRoleDto createRoleDto){
        Role role = roleService.save(createRoleDto);

        return ResponseEntity.ok(role);
    }

    @ApiOperation(value = SWG_ROLE_LIST_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_LIST_MESSAGE, response = RoleListResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasAuthority('read:roles')")
    @GetMapping
    public ResponseEntity<RoleListResponse> all(){
        return ResponseEntity.ok(new RoleListResponse(roleService.findAll()));
    }

    @ApiOperation(value = SWG_ROLE_ITEM_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_ITEM_MESSAGE, response = RoleResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasAuthority('read:role')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> one(@PathVariable String id) throws ResourceNotFoundException {
        Role role = roleService.findById(id);

        return ResponseEntity.ok(new RoleResponse(role));
    }

    @ApiOperation(value = SWG_ROLE_UPDATE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_UPDATE_MESSAGE, response = RoleResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('update:role')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable String id, @Valid @RequestBody CreateRoleDto createRoleDto)
        throws ResourceNotFoundException {
        return ResponseEntity.ok(new RoleResponse(roleService.update(id, createRoleDto)));
    }

    @ApiOperation(value = SWG_ROLE_DELETE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = SWG_ROLE_DELETE_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasAuthority('delete:role')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        roleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = SWG_ROLE_ASSIGN_PERMISSION_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_ASSIGN_PERMISSION_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('add:permission')")
    @PutMapping("/{id}/permissions")
    public ResponseEntity<RoleResponse> addPermissions(@PathVariable String id, @Valid @RequestBody UpdateRolePermissionDto updateRolePermissionDto)
        throws ResourceNotFoundException {
        Role role = roleService.findById(id);

        Arrays.stream(updateRolePermissionDto.getPermissions()).forEach(permissionName -> {
            Optional<Permission> permission = permissionService.findByName(permissionName);

            if (permission.isPresent() && !role.hasPermission(permissionName)) {
                role.addPermission(permission.get());
            }
        });

        Role roleUpdated = roleService.update(role);

        return ResponseEntity.ok().body(new RoleResponse(roleUpdated));
    }

    @ApiOperation(value = SWG_ROLE_REMOVE_PERMISSION_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_REMOVE_PERMISSION_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasAuthority('remove:permission')")
    @DeleteMapping("/{id}/permissions")
    public ResponseEntity<RoleResponse> removePermissions(@PathVariable String id, @Valid @RequestBody UpdateRolePermissionDto updateRolePermissionDto)
        throws ResourceNotFoundException {
        Role role = roleService.findById(id);

        Arrays.stream(updateRolePermissionDto.getPermissions()).forEach(permissionName -> {
            Optional<Permission> permission = permissionService.findByName(permissionName);

            if (permission.isPresent() && role.hasPermission(permissionName)) {
                role.removePermission(permission.get());
            }
        });

        Role roleUpdated = roleService.update(role);

        return ResponseEntity.ok().body(new RoleResponse(roleUpdated));
    }
}
