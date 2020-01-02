package com.tericcabrel.authorization.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "roles")
public class Role extends BaseModel {
    @Field(name = "name")
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Role setDescription(String description) {
        this.description = description;
        return this;
    }
}
