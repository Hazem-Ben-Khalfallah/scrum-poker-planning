package com.blacknebula.scrumpoker.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author hazem
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtPayload {
    @JsonProperty("sub")
    private String username;
    @JsonProperty("sId")
    private String sessionId;
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
