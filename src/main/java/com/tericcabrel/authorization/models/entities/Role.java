package com.tericcabrel.authorization.models.entities;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@Document(collection = "roles")
public class Role extends BaseModel {
    @Field(name = "name")
    private String name;

    private String description;

    private boolean isDefault;

    @DBRef
    private Set<Permission> permissions;

    public Role() {
        permissions = new HashSet<>();
    }

    public Role addPermission(Permission permission) {
        this.permissions.add(permission);

        return this;
    }

    public boolean hasPermission(String permissionName) {
        Optional<Permission> permissionItem = this.permissions.stream().filter(permission -> permission.getName().equals(permissionName)).findFirst();

        return permissionItem.isPresent();
    }

    public Role removePermission(Permission permission) {
        Stream<Permission> newPermissions = this.permissions.stream().filter(permission1 -> !permission1.getName().equals(permission.getName()));

        this.permissions = newPermissions.collect(Collectors.toSet());

        return this;
    }
}
