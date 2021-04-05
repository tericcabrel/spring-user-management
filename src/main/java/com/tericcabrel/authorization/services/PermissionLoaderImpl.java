package com.tericcabrel.authorization.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tericcabrel.authorization.models.dtos.PermissionLoadDto;
import com.tericcabrel.authorization.models.entities.Permission;
import com.tericcabrel.authorization.models.entities.Role;
import com.tericcabrel.authorization.models.enums.PermissionLoadMode;
import com.tericcabrel.authorization.repositories.PermissionRepository;
import com.tericcabrel.authorization.repositories.RoleRepository;
import com.tericcabrel.authorization.services.interfaces.PermissionLoader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
public class PermissionLoaderImpl implements PermissionLoader {

  private final Log logger = LogFactory.getLog(this.getClass());

  @Value("${app.permission.load.mode}")
  private PermissionLoadMode loadMode;

  private final RoleRepository roleRepository;

  private final PermissionRepository permissionRepository;

  public PermissionLoaderImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
  }

  private void addPermissionToRole(Permission permission, String[] roleNames) {
    Arrays.stream(roleNames).parallel().forEach(roleName -> {
      Optional<Role> role = roleRepository.findByName(roleName);

      role.ifPresent(roleFound -> {
        if (!roleFound.hasPermission(permission.getName())) {
          roleFound.addPermission(permission);

          roleRepository.save(roleFound);
        }
      });
    });
  }

  private void loadPermissions(List<PermissionLoadDto> permissionLoadDtoList) {
    permissionLoadDtoList.parallelStream().forEach(permissionLoadDto -> {
      Permission permissionCreated;
      Optional<Permission> permission = permissionRepository.findByName(permissionLoadDto.getName());

      if (permission.isEmpty()) {
        permissionCreated = permissionRepository.save(permissionLoadDto.toPermission());
      } else {
        permissionCreated = permission.get();
      }

      addPermissionToRole(permissionCreated, permissionLoadDto.getRoleNames());
    });
  }

  @Override
  public void load() {
    List<PermissionLoadDto> permissionLoadDtoList;

    if (loadMode.equals(PermissionLoadMode.CREATE)) {
      permissionRepository.deleteAll();
    }

    Resource resource = new ClassPathResource("permission.json");

    try (InputStream inputStream = resource.getInputStream()) {

      byte[] binaryData = FileCopyUtils.copyToByteArray(inputStream);
      String data = new String(binaryData, StandardCharsets.UTF_8);

      Type permissionLoadDtoListType = new TypeToken<ArrayList<PermissionLoadDto>>() {
      }.getType();

      permissionLoadDtoList = new Gson().fromJson(data, permissionLoadDtoListType);

      loadPermissions(permissionLoadDtoList);
    } catch (IOException ignored) {
      logger.error("Loading permissions: failed to read permission file!");
    }
  }
}
