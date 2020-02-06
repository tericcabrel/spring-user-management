package com.tericcabrel.authorization.models.mongo;

import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

public abstract class BaseModel {
    @MongoId(FieldType.OBJECT_ID)
    protected String id;

    protected Date createdAt;

    protected Date updatedAt;

    public String getId() {
        return id;
    }

    public BaseModel setId(String id) {
        this.id = id;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public BaseModel setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public BaseModel setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
