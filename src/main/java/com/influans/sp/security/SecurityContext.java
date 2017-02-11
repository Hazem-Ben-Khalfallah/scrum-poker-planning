package com.influans.sp.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author hazem
 */
@Component
@Profile("!test")
public class SecurityContext {
    public Optional<Principal> getAuthenticationContext() {
        if (SecurityContextHolder.getContext().getAuthentication() == null)
            return Optional.empty();

        final Principal principal = ((ScrumPokerAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        return Optional.of(principal);
    }

    public class Headers {
        public static final String AUTHORIZATION = "Authorization";
        public static final String JWT_TOKEN = "jwt-token";
    }

}
