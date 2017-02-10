package com.influans.sp.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author hazem
 */
@Component
public class SecurityContext {
    public Principal getAuthenticationContext() {
        return ((ScrumPokerAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }

    public void removeAuthenticationContext() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
