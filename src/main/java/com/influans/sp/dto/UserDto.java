package com.influans.sp.dto;

/**
 * @author hazem
 */
public class UserDto {
    private String name;
    private String sessionId;
    private boolean isAdmin;

    public UserDto(String name, String sessionId, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.name = name;
        this.sessionId = sessionId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
