package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.dto.UserDto;

/**
 * @author hazem
 */
public class UserDtoBuilder {
    private String username;
    private String sessionId;
    private boolean admin;

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public UserDtoBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserDtoBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }


    public UserDtoBuilder withAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public UserDto build() {
        final UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setSessionId(sessionId);
        userDto.setAdmin(admin);
        return userDto;
    }
}
