package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.security.Principal;

/**
 * @author hazem
 */
public class PrincipalBuilder {
    private String username;
    private String sessionId;
    private UserRole role;

    public static PrincipalBuilder builder() {
        return new PrincipalBuilder();
    }

    public PrincipalBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public PrincipalBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }


    public PrincipalBuilder withRole(UserRole role) {
        this.role = role;
        return this;
    }

    public Principal build() {
        return new Principal(username, sessionId, role);
    }
}
