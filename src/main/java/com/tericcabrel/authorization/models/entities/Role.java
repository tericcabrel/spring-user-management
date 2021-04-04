package com.tericcabrel.authorization.models.entities;

import java.util.HashSet;
import java.util.Set;
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

    @DBRef
    private Set<Permission> permissions;

    public Role() {
        permissions = new HashSet<>();
    }
}
