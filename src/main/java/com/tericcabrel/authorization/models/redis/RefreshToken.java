package com.tericcabrel.authorization.models.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    private String id;

    @Indexed
    private String value;

    public RefreshToken(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public RefreshToken setId(String id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public RefreshToken setValue(String value) {
        this.value = value;
        return this;
    }
}