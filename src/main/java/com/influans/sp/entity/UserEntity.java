package com.influans.sp.entity;

import com.influans.sp.entity.def.UserEntityDef;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
public class UserEntity {
    @Id
    private EntityId userId;
    @Field(UserEntityDef.ADMIN)
    private Boolean isAdmin;

    public UserEntity() {
    }

    public UserEntity(String username, String sessionId, Boolean isAdmin) {
        this.userId = new EntityId(username, sessionId);
        this.isAdmin = isAdmin;

    }


    public EntityId getUserId() {
        return userId;
    }

    public void setUserId(EntityId userId) {
        this.userId = userId;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

}