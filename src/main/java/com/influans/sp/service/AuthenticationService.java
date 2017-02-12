package com.influans.sp.service;

import com.influans.sp.entity.UserEntity;
import com.influans.sp.enums.UserRole;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.security.Principal;
import com.influans.sp.security.SecurityContext;
import com.influans.sp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author hazem
 */
@Service
public class AuthenticationService {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityContext securityContext;

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

        if (!sessionRepository.exists(user.getSessionId())) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "session not found with id = " + user.getSessionId());
        }

        final UserEntity userEntity = userRepository.findUser(user.getSessionId(), user.getUsername());
        if (userEntity == null) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "user not found with username %s in session %s ",
                    user.getUsername(), user.getSessionId());
        } else if (!userEntity.isConnected()) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "user %s has been disconnected  from session %s ",
                    user.getUsername(), user.getSessionId());
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
