package com.tericcabrel.authorization.models.mongo;

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
    private Set<Role> roles;

    public User() {
        roles = new HashSet<>();
    }

    public User(String firstName, String lastName, String email, String password, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.enabled = true;
        this.confirmed = false;
        roles = new HashSet<>();
    }

    public User addRole(Role role) {
        this.roles.add(role);

        return this;
    }

    public boolean hasRole(String roleName) {
        Optional<Role> roleItem = this.roles.stream().filter(role -> role.getName().equals(roleName)).findFirst();

        return roleItem.isPresent();
    }

    public User removeRole(Role role) {
        Stream<Role> newRoles = this.roles.stream().filter(role1 -> !role1.getName().equals(role.getName()));

        this.roles = newRoles.collect(Collectors.toSet());

        return this;
    }
}
