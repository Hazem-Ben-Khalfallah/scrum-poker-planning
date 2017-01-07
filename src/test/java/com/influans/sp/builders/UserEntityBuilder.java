package com.influans.sp.builders;

import com.influans.sp.entity.EntityId;
import com.influans.sp.entity.UserEntity;

/**
 * @author hazem
 */
public class UserEntityBuilder {

    private String username;
    private String sessionId;

    public static UserEntityBuilder builder() {
        return new UserEntityBuilder();
    }

    public UserEntityBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserEntityBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }


    public UserEntity build() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUserId(new EntityId(username, sessionId));
        return userEntity;
    }
}
