package com.tericcabrel.authorization.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@Document(collection = "users")
public class User extends BaseModel {
    private String firstName;

    private String lastName;

    private String gender;

    @Field("email")
    private String email;

    @JsonIgnore
    private String password;

    private boolean enabled;

    private boolean confirmed;

    private String avatar;

    private String timezone;

    private Coordinates coordinates;

    @DBRef
    private Role role;

    @DBRef
    private Set<Permission> permissions;

    public User() {
        permissions = new HashSet<>();
    }

    public User(String firstName, String lastName, String email, String password, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.enabled = true;
        this.confirmed = false;
        permissions = new HashSet<>();
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);

    }

    public boolean hasPermission(String permissionName) {
        Optional<Permission> permissionItem = this.permissions.stream().filter(permission -> permission.getName().equals(permissionName)).findFirst();

        return permissionItem.isPresent();
    }

    public void removePermission(Permission permission) {
        Stream<Permission> newPermissions = this.permissions.stream().filter(permission1 -> !permission1.getName().equals(permission.getName()));

        this.permissions = newPermissions.collect(Collectors.toSet());

    }

    public Set<Permission> allPermissions() {
        Set<Permission> userPermissions = this.permissions;
        Set<Permission> userRolePermissions = this.role.getPermissions();

        Set<Permission> all = new HashSet<>(userPermissions);
        all.addAll(userRolePermissions);

        return all;
    }
}
