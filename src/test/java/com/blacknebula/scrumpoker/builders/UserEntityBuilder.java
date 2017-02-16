package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.entity.EntityId;
import com.blacknebula.scrumpoker.entity.UserEntity;

/**
 * @author hazem
 */
public class UserEntityBuilder {

    private String username;
    private String sessionId;
    private boolean connected = true;
    private boolean admin;

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


    public UserEntityBuilder withConnected(boolean connected) {
        this.connected = connected;
        return this;
    }

    public UserEntityBuilder withAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public UserEntity build() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUserId(new EntityId(username, sessionId));
        userEntity.setConnected(connected);
        userEntity.setAdmin(admin);
        return userEntity;
    }
}
