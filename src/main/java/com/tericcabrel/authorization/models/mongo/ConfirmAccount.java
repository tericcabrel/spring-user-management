package com.tericcabrel.authorization.models.mongo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@Document(collection = "accounts_confirmation")
public class ConfirmAccount extends BaseModel {
    @DBRef
    private User user;

    private String token;

    private long expireAt;
}
