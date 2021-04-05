package com.tericcabrel.authorization.controllers;

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
import com.tericcabrel.authorization.models.dtos.UpdateRoleDto;
import com.tericcabrel.authorization.models.entities.Role;
import com.tericcabrel.authorization.models.entities.User;
import com.tericcabrel.authorization.services.interfaces.RoleService;
import com.tericcabrel.authorization.services.interfaces.UserService;

@Api(tags = SWG_ROLE_TAG_NAME, description = SWG_ROLE_TAG_DESCRIPTION)
@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    private final RoleService roleService;

    private final UserService userService;

    public RoleController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ApiOperation(value = SWG_ROLE_CREATE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_CREATE_MESSAGE, response = Role.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> one(@PathVariable String id){
        return ResponseEntity.ok(new RoleResponse(roleService.findById(id)));
    }

    @ApiOperation(value = SWG_ROLE_UPDATE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_UPDATE_MESSAGE, response = RoleResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable String id, @Valid @RequestBody CreateRoleDto createRoleDto) {
        return ResponseEntity.ok(new RoleResponse(roleService.update(id, createRoleDto)));
    }

    @ApiOperation(value = SWG_ROLE_DELETE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = SWG_ROLE_DELETE_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        roleService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = SWG_ROLE_ASSIGN_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_ASSIGN_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/assign")
    public ResponseEntity<UserResponse> assignRoles(@Valid @RequestBody UpdateRoleDto updateRoleDto) {
        User user = userService.findById(updateRoleDto.getUserId());

        Arrays.stream(updateRoleDto.getRoles()).forEach(role -> {
            Optional<Role> roleObject = roleService.findByName(role);

            if (roleObject.isPresent() && !user.hasRole(role)) {
                user.addRole(roleObject.get());
            }
        });

        userService.update(user);

        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @ApiOperation(value = SWG_ROLE_REVOKE_OPERATION, response = SuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = SWG_ROLE_REVOKE_MESSAGE, response = UserResponse.class),
        @ApiResponse(code = 401, message = UNAUTHORIZED_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 403, message = FORBIDDEN_MESSAGE, response = BadRequestResponse.class),
        @ApiResponse(code = 422, message = INVALID_DATA_MESSAGE, response = InvalidDataResponse.class),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/revoke")
    public ResponseEntity<User> revokeRoles(@Valid @RequestBody UpdateRoleDto updateRoleDto) {
        User user = userService.findById(updateRoleDto.getUserId());

        Arrays.stream(updateRoleDto.getRoles()).forEach(role -> {
            Optional<Role> roleObject = roleService.findByName(role);

            if (roleObject.isPresent() && user.hasRole(role)) {
                user.removeRole(roleObject.get());
            }
        });

        userService.update(user);

        return ResponseEntity.ok().body(user);
    }
}
