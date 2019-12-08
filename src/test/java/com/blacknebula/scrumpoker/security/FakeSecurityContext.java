package com.blacknebula.scrumpoker.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author hazem
 */
@Component
@Profile("test")
public class FakeSecurityContext implements SecurityContext {
    private Principal principal;

    public Optional<Principal> getAuthenticationContext() {
        return Optional.ofNullable(principal);
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}
