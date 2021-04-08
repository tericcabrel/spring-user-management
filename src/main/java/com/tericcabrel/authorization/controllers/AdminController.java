package com.tericcabrel.authorization.controllers;

import static com.tericcabrel.authorization.utils.Constants.ROLE_ADMIN;
import static com.tericcabrel.authorization.utils.Constants.ROLE_NOT_FOUND_MESSAGE;
import com.tericcabrel.authorization.exceptions.ResourceNotFoundException;
import com.tericcabrel.authorization.models.dtos.CreateUserDto;
import com.tericcabrel.authorization.models.entities.Role;
import com.tericcabrel.authorization.models.entities.User;
import com.tericcabrel.authorization.models.response.UserResponse;
import com.tericcabrel.authorization.services.interfaces.RoleService;
import com.tericcabrel.authorization.services.interfaces.UserService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admins")
public class AdminController {
  private final RoleService roleService;

  private final UserService userService;


  public AdminController(RoleService roleService, UserService userService) {
    this.roleService = roleService;
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
  @PostMapping(value = "")
  public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserDto createUserDto)
      throws ResourceNotFoundException {
    Optional<Role> roleUser = roleService.findByName(ROLE_ADMIN);

    if (roleUser.isEmpty()) {
      throw new ResourceNotFoundException(ROLE_NOT_FOUND_MESSAGE);
    }

    createUserDto.setRole(roleUser.get())
        .setConfirmed(true)
        .setEnabled(true);

    User user = userService.save(createUserDto);

    return ResponseEntity.ok(new UserResponse(user));
  }

  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    userService.delete(id);

    return ResponseEntity.noContent().build();
  }
}
