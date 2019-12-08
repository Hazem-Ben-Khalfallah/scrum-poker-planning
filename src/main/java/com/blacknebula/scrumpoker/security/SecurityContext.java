package com.blacknebula.scrumpoker.security;

import java.util.Optional;

/**
 * @author hazem
 */
public interface SecurityContext {
    Optional<Principal> getAuthenticationContext();

    void setPrincipal(Principal principal);

    class Headers {
        public static final String AUTHORIZATION = "Authorization";
        public static final String JWT_TOKEN = "jwt-token";
    }
}
