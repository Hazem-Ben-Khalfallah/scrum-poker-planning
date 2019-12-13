package com.blacknebula.scrumpoker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author hazem
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String username;
    private String sessionId;
    private boolean isAdmin;

    public UserDto() {
    }

    public UserDto(String username, String sessionId, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.username = username;
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public UserDto setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public UserDto setAdmin(boolean admin) {
        isAdmin = admin;
        return this;
    }
}
