package com.tericcabrel.authorization.controllers;

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
import com.tericcabrel.authorization.models.common.ApiResponse;
import com.tericcabrel.authorization.services.interfaces.IRoleService;
import com.tericcabrel.authorization.services.interfaces.IUserService;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    private IRoleService roleService;

    private IUserService userService;

    public RoleController(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody RoleDto roleDto){
        Role role = roleService.save(roleDto);

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), role));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> all(){
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), roleService.findAll()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> one(@PathVariable String id){
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), roleService.findById(id)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable String id, @RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), roleService.update(id, roleDto)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        roleService.delete(id);

        return ResponseEntity.noContent().build();
    }

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
