package com.blacknebula.scrumpoker.entity;

import com.blacknebula.scrumpoker.entity.def.UserEntityDef;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Optional;

@Document(collection = "user")
public class UserEntity {
    @Id
    private EntityId userId;
    @Field(UserEntityDef.ADMIN)
    private Boolean admin;
    @Field(UserEntityDef.CONNECTED)
    private Boolean connected;

    public UserEntity() {
    }

    public UserEntity(String username, String sessionId, Boolean admin) {
        this.userId = new EntityId(username, sessionId);
        this.admin = admin;
        this.connected = true;
    }


    public EntityId getUserId() {
        return userId;
    }

    public void setUserId(EntityId userId) {
        this.userId = userId;
    }

    public Boolean isAdmin() {
        return Optional.ofNullable(admin).orElse(false);
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean isConnected() {
        return Optional.ofNullable(connected).orElse(false);
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}