package com.tericcabrel.authorization.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("refreshToken")
@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class RefreshToken {
    @Id
    private String id;

    @Indexed
    private String value;
}