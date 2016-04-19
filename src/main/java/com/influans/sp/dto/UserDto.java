package com.influans.sp.dto;

/**
 * @author hazem
 */
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
