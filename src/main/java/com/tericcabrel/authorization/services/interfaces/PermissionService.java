package com.tericcabrel.authorization.services.interfaces;

import com.tericcabrel.authorization.models.entities.Permission;
import java.util.List;
import java.util.Optional;

public interface PermissionService {
  List<Permission> findAll();

  Optional<Permission> findById(String id);

  Optional<Permission> findByName(String id);
}
