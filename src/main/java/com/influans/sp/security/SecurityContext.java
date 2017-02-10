package com.influans.sp.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author hazem
 */
@Component
@Profile("!test")
public class SecurityContext {
    public Principal getAuthenticationContext() {
        return ((ScrumPokerAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }

    public class Headers {
        public static final String AUTHORIZATION = "Authorization";
        public static final String JWT_TOKEN = "jwt-token";
    }

}
