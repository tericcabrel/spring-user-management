package com.tericcabrel.authorization.models.entities;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@Document(collection = "users_accounts")
public class UserAccount extends BaseModel {
    @DBRef
    private User user;

    private String token;

    private long expireAt;

    public boolean isExpired() {
        return expireAt < new Date().getTime();
    }
}
