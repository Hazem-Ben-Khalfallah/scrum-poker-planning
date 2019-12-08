package com.blacknebula.scrumpoker.security;

import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author hazem
 */
@Component
@Profile("!test")
public class SecurityContextImpl implements SecurityContext {

    @Override
    public Optional<Principal> getAuthenticationContext() {
        if (SecurityContextHolder.getContext().getAuthentication() == null)
            return Optional.empty();

        final Principal principal = ((ScrumPokerAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        return Optional.of(principal);
    }

    @Override
    public void setPrincipal(Principal principal) {
        throw new CustomException(CustomErrorCode.SERVICE_UNAVAILABLE);
    }

}
