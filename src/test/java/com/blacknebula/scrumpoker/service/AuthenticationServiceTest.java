package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.ApplicationTest;
import com.blacknebula.scrumpoker.builders.PrincipalBuilder;
import com.blacknebula.scrumpoker.builders.SessionEntityBuilder;
import com.blacknebula.scrumpoker.builders.UserEntityBuilder;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.security.SecurityContext;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hazem
 */
public class AuthenticationServiceTest extends ApplicationTest {
    @Autowired
    private SecurityContext securityContext;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @verifies throw an exception if user is not authenticated
     * @see AuthenticationService#checkAuthenticatedUser()
     */
    @Test
    public void checkAuthenticatedUser_shouldThrowAnExceptionIfUserIsNotAuthenticated() throws Exception {
        securityContext.setPrincipal(null);

        try {
            // when
            authenticationService.checkAuthenticatedUser();
            Assert.fail("shouldThrowAnExceptionIfUserIsNotAuthenticated");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * @verifies throw an exception if username is null or empty
     * @see AuthenticationService#checkAuthenticatedUser()
     */
    @Test
    public void checkAuthenticatedUser_shouldThrowAnExceptionIfUsernameIsNullOrEmpty() throws Exception {
        // given
        final Principal principal = PrincipalBuilder.builder()
                .withSessionId("sessionId")
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);
        try {
            // when
            authenticationService.checkAuthenticatedUser();
            Assert.fail("shouldThrowAnExceptionIfUsernameIsNullOrEmpty");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * @verifies throw an exception if sessionId is null or empty
     * @see AuthenticationService#checkAuthenticatedUser()
     */
    @Test
    public void checkAuthenticatedUser_shouldThrowAnExceptionIfSessionIdIsNullOrEmpty() throws Exception {
        /// given
        final Principal principal = PrincipalBuilder.builder()
                .withUsername("username")
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);
        try {
            // when
            authenticationService.checkAuthenticatedUser();
            Assert.fail("shouldThrowAnExceptionIfSessionIdIsNullOrEmpty");
        } catch (CustomException e) {
            //then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * @verifies throw an exception if no user has been connected to the related session with the given username
     * @see AuthenticationService#checkAuthenticatedUser()
     */
    @Test
    public void checkAuthenticatedUser_shouldThrowAnExceptionIfNoUserHasBeenConnectedToTheRelatedSessionWithTheGivenUsername() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId("other_session_id")
                .build();
        userRepository.save(userEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);

        try {
            authenticationService.checkAuthenticatedUser();
            Assert.fail("shouldThrowAnExceptionIfUserDoesNotExistWithGivenUsername");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringWhitespace("Invalid user credentials");
        }
    }

    /**
     * @verifies throw an exception if user has been disconnected from the related session
     * @see AuthenticationService#checkAuthenticatedUser()
     */
    @Test
    public void checkAuthenticatedUser_shouldThrowAnExceptionIfUserHasBeenDisconnectedFromTheRelatedSession() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "username";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(false)
                .build();
        userRepository.save(userEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);

        try {
            // when
            authenticationService.checkAuthenticatedUser();
            Assert.fail("shouldThrowAnExceptionIfUserHasBeenDisconnectedFromTheRelatedSession");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringWhitespace("User already disconnected from session");
        }
    }

    /**
     * @verifies throw an exception if session does not exist with given sessionId
     * @see AuthenticationService#checkAuthenticatedUser()
     */
    @Test
    public void checkAuthenticatedUser_shouldThrowAnExceptionIfSessionDoesNotExistWithGivenSessionId() throws Exception {
        // given
        final Principal principal = PrincipalBuilder.builder()
                .withUsername("username")
                .withSessionId("invalid_session_id")
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);
        try {
            authenticationService.checkAuthenticatedUser();
            Assert.fail("shouldThrowAnExceptionIfSessionDoesNotExistWithGivenSessionId");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringWhitespace("Invalid session id");
        }
    }
}
