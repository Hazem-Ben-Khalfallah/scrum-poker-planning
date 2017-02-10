package com.influans.sp.security;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenAuthenticationFilter.class);
    private static final Pattern JWT_AUTH_HEADER = Pattern.compile("Bearer (.*)", Pattern.CASE_INSENSITIVE);

    private final Set<Pair<String, String>> PERMITTED_URL = ImmutableSet.<Pair<String, String>>builder()
            .add(ImmutablePair.of("POST", "/users/connect"))
            .add(ImmutablePair.of("POST", "/users/disconnect"))
            .build();

    @Autowired
    private JwtService jwtService;


    @Override
    // fixme don't use SecurityContextHolder statically.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        if (isPermitted(request) || authorizationHeader == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
        } else {
            Authentication authentication = decode(authorizationHeader);
            if (authentication == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                try {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }

    private boolean isPermitted(HttpServletRequest request) {
        final String apiURI = getApiURI(request);
        final String method = getMethod(request);
        LOGGER.info("Endpoint: {} {}", method, apiURI);
        return PERMITTED_URL.contains(ImmutablePair.of(method, apiURI));
    }

    private String getApiURI(HttpServletRequest request) {
        if (request.getRequestURI() == null) {
            return "";
        }
        if (request.getContextPath() == null) {
            return request.getRequestURI();
        }
        return request.getRequestURI().substring(request.getContextPath().length());
    }

    private String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }


    private Authentication decode(String authorizationHeader) {
        String token = obtainJwtToken(authorizationHeader);
        if (token == null) {
            LOGGER.warn("Access refused: invalid Authorization header" + authorizationHeader);
            return null;
        }
        return jwtService.authenticate(token);
    }

    private String obtainJwtToken(String authorizationHeader) {
        Matcher apiMatcher = JWT_AUTH_HEADER.matcher(authorizationHeader);
        if (apiMatcher.matches()) {
            return apiMatcher.group(1);
        }
        return null;
    }

}
