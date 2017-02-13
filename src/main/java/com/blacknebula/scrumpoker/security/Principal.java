package com.blacknebula.scrumpoker.security;

import com.blacknebula.scrumpoker.enums.UserRole;

public class Principal {

    private final String username;
    private final String sessionId;
    private final UserRole role;

    public Principal(String username, String sessionId, UserRole role) {
        this.username = username;
        this.sessionId = sessionId;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public UserRole getRole() {
        return role;
    }


}
