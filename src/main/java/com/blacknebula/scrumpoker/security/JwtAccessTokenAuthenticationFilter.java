package com.blacknebula.scrumpoker.security;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.lanwen.verbalregex.VerbalExpression;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Profile("!test")
public class JwtAccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAccessTokenAuthenticationFilter.class);
    private static final Pattern JWT_AUTH_HEADER = Pattern.compile("Bearer (.*)", Pattern.CASE_INSENSITIVE);

    private final Set<Pair<String, VerbalExpression>> PERMITTED_URL = ImmutableSet.<Pair<String, VerbalExpression>>builder()
            .add(ImmutablePair.of("POST", VerbalExpression.regex().startOfLine().then("/users/connect").endOfLine().build()))
            .add(ImmutablePair.of("POST", VerbalExpression.regex().startOfLine().then("/sessions").endOfLine().build()))
            .build();

    @Autowired
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(SecurityContext.Headers.AUTHORIZATION);

        if (isPermitted(request) || authorizationHeader == null) {
            filterChain.doFilter(request, response);
        } else {
            final ScrumPokerAuthenticationToken authentication = decode(authorizationHeader);
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
        return PERMITTED_URL.stream().anyMatch(stringVerbalExpressionPair ->
                stringVerbalExpressionPair.getKey().equals(method) &&
                        stringVerbalExpressionPair.getValue().testExact(apiURI));
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


    private ScrumPokerAuthenticationToken decode(String authorizationHeader) {
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
