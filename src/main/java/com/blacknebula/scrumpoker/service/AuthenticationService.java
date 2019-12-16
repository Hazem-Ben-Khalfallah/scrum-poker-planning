package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.security.SecurityContext;
import com.blacknebula.scrumpoker.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author hazem
 */
@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SecurityContext securityContext;

    public AuthenticationService(SessionRepository sessionRepository,
                                 UserRepository userRepository,
                                 SecurityContext securityContext) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    /**
     * @return connect user or throws an UNAUTHORIZED Exception
     * @should throw an exception if user is not authenticated
     * @should throw an exception if username is null or empty
     * @should throw an exception if sessionId is null or empty
     * @should throw an exception if no user has been connected to the related session with the given username
     * @should throw an exception if user has been disconnected from the related session
     * @should throw an exception if session does not exist with given sessionId
     */
    public Principal checkAuthenticatedUser() {
        final Optional<Principal> optional = securityContext.getAuthenticationContext();

        if (!optional.isPresent()) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated");
        }

        final Principal user = optional.get();

        if (StringUtils.isEmpty(user.getSessionId())) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "sessionId should not be null or empty");
        }

        if (StringUtils.isEmpty(user.getUsername())) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "username should not be null or empty");
        }

        if (!sessionRepository.existsById(user.getSessionId())) {
            LOGGER.error("session not found with id = {}", user.getSessionId());
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "Invalid session id");
        }

        final UserEntity userEntity = userRepository.findUser(user.getSessionId(), user.getUsername());
        if (userEntity == null) {
            LOGGER.error("user not found with username {} in session {}", user.getUsername(), user.getSessionId());
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "Invalid user credentials");
        } else if (!userEntity.isConnected()) {
            LOGGER.error("user {} has been disconnected  from session {}", user.getUsername(), user.getSessionId());
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "User already disconnected from session");
        }
        return user;
    }

    public Principal checkAuthenticatedAdmin() {
        final Principal principal = checkAuthenticatedUser();
        if (!UserRole.SESSION_ADMIN.equals(principal.getRole())) {
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "user has not session admin role");
        }
        return principal;
    }
}
