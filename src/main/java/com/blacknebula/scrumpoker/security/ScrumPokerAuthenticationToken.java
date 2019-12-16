package com.blacknebula.scrumpoker.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.google.common.collect.Lists.newArrayList;

public class ScrumPokerAuthenticationToken extends AbstractAuthenticationToken {

    private final Principal principal;
    private Object credentials;

    public ScrumPokerAuthenticationToken(Principal principal) {
        super(newArrayList(new SimpleGrantedAuthority(principal.getRole().name())));
        this.principal = principal;
        setAuthenticated(false);
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}
