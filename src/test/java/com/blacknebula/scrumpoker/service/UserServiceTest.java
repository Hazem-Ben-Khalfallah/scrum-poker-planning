package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.ApplicationTest;
import com.blacknebula.scrumpoker.builders.PrincipalBuilder;
import com.blacknebula.scrumpoker.builders.SessionEntityBuilder;
import com.blacknebula.scrumpoker.builders.UserDtoBuilder;
import com.blacknebula.scrumpoker.builders.UserEntityBuilder;
import com.blacknebula.scrumpoker.dto.UserDto;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.enums.WsTypes;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.JwtPayload;
import com.blacknebula.scrumpoker.security.JwtService;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.security.SecurityContext;
import com.blacknebula.scrumpoker.websocket.WebSocketSender;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.verify;

/**
 * @author hazem
 */
public class UserServiceTest extends ApplicationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebSocketSender webSocketSender;
    @Autowired
    private SecurityContext securityContext;
    @Autowired
    private JwtService jwtService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(webSocketSender);
    }

    /**
     * @verifies check that the user is authenticated
     * @see UserService#listUsers()
     */
    @Test
    public void listUsers_shouldCheckThatTheUserIsAuthenticated() throws Exception {
        //given
        securityContext.setPrincipal(null);
        try {
            // when
            userService.listUsers();
            Assert.fail("shouldCheckThatTheUserIsAuthenticated");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * @verifies return connected users only
     * @see UserService#listUsers()
     */
    @Test
    public void listUsers_shouldReturnConnectedUsersOnly() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final List<UserEntity> users = ImmutableList.<UserEntity>builder()
                .add(UserEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withUsername(username)
                        .withConnected(true)
                        .build())
                .add(UserEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withUsername("Leander")
                        .withConnected(false)
                        .build())
                .build();
        userRepository.saveAll(users);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        // when
        final List<UserDto> connectedUsers = userService.listUsers();

        // then
        Assertions.assertThat(connectedUsers).hasSize(1);
        Assertions.assertThat(connectedUsers.get(0).getUsername()).isEqualTo(username);
    }

    /**
     * @verifies throw and error if sessionId is null or empty
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldThrowAndErrorIfSessionIdIsNullOrEmpty() throws Exception {
        final UserDto userDto = UserDtoBuilder.builder()
                .withUsername("Leo")
                .build();
        try {
            userService.connectUser(userDto, (t) -> {
            });
            Assert.fail("shouldThrowAndErrorIfSessionIdIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
            Assertions.assertThat(e.getMessage()).isEqualTo("Session should not be null or empty");
        }
    }

    /**
     * @verifies throw and error if username is null or empty
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldThrowAndErrorIfUsernameIsNullOrEmpty() throws Exception {
        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId("sessionId")
                .build();
        try {
            userService.connectUser(userDto, (t) -> {
            });
            Assert.fail("shouldThrowAndErrorIfUsernameIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringWhitespace("Username should not be null or empty");
        }
    }

    /**
     * @verifies throw and error if username contains only spaces
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldThrowAndErrorIfUsernameContainsOnlySpaces() throws Exception {
        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId("sessionId")
                .withUsername("   ")
                .build();
        try {
            userService.connectUser(userDto, (t) -> {
            });
            Assert.fail("shouldThrowAndErrorIfUsernameContainsOnlySpaces");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw and error if withSessionId is not valid
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldThrowAndErrorIfSessionIdIsNotValid() throws Exception {
        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId("invalid_session_id")
                .withUsername("Leo")
                .build();
        try {
            userService.connectUser(userDto, (t) -> {
            });
            Assert.fail("shouldThrowAndErrorIfSessionIdIsNotValid");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
            Assertions.assertThat(e.getMessage()).isEqualTo("Invalid session id");
        }
    }

    /**
     * @verifies create new user if withSessionId and withUsername are valid
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldCreateNewUserIfSessionIdAndUsernameAreValid() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId(sessionId)
                .withUsername("Leo")
                .build();

        // when
        userService.connectUser(userDto, (t) -> {
        });

        // then
        final UserEntity userEntity = userRepository.findUser(userDto.getSessionId(), userDto.getUsername());
        Assertions.assertThat(userEntity).isNotNull();
        Assertions.assertThat(userEntity.isAdmin()).isFalse();
    }

    /**
     * @verifies throw an exception if username already used in  the given sessionId with connected status
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldThrowAnExceptionIfUsernameAlreadyUsedInTheGivenSessionIdWithConnectedStatus() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity existingConnectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(existingConnectedUser);

        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId(sessionId)
                .withUsername(username)
                .build();

        try {
            userService.connectUser(userDto, (t) -> {
            });
            Assert.fail("shouldThrowAnExceptionIfUsernameAlreadyUsedInTheGivenSessionIdWithConnectedStatus");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.DUPLICATE_IDENTIFIER);
            Assertions.assertThat(e.getMessage()).isEqualTo("Username already used");
        }
    }

    /**
     * @verifies reconnect user and generate valid token if he was previously disconnected
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldReconnectUserAndGenerateValidTokenIfHeWasPreviouslyDisconnected() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity existingDisconnectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(false)
                .withAdmin(false)
                .build();
        userRepository.save(existingDisconnectedUser);

        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId(sessionId)
                .withUsername(username)
                .build();

        // when
        UserDto result = userService.connectUser(userDto, (token) -> {
            // then
            checkJwtToken(token, userDto.getUsername(), userDto.getSessionId(), UserRole.VOTER);
        });

        // then
        Assertions.assertThat(result.isAdmin()).isFalse();
        final List<UserEntity> users = userRepository.findUsersBySessionId(userDto.getSessionId());
        Assertions.assertThat(users).hasSize(1);
        Assertions.assertThat(users.get(0).isConnected()).isTrue();
        Assertions.assertThat(users.get(0).isAdmin()).isFalse();
    }

    /**
     * @verifies reconnect admin and generate valid token if he has previously logged out
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldReconnectAdminAndGenerateValidTokenIfHeHasPreviouslyLoggedOut() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity disconnectedAdmin = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(false)
                .withAdmin(true)
                .build();
        userRepository.save(disconnectedAdmin);

        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId(sessionId)
                .withUsername(username)
                .build();

        // when
        final UserDto result = userService.connectUser(userDto, (token) -> {
            // then
            checkJwtToken(token, userDto.getUsername(), userDto.getSessionId(), UserRole.SESSION_ADMIN);
        });

        // then
        Assertions.assertThat(result.isAdmin()).isTrue();
        final List<UserEntity> users = userRepository.findUsersBySessionId(userDto.getSessionId());
        Assertions.assertThat(users).hasSize(1);
        Assertions.assertThat(users.get(0).isConnected()).isTrue();
        Assertions.assertThat(users.get(0).isAdmin()).isTrue();
    }

    /**
     * @verifies send a websocket notification
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldSendAWebsocketNotification() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId(sessionId)
                .withUsername("Leo")
                .build();

        // when
        userService.connectUser(userDto, (t) -> {
        });

        // then
        verify(webSocketSender).sendNotification(userDto.getSessionId(), WsTypes.USER_CONNECTED, userDto);
    }

    /**
     * @verifies check that the user is authenticated
     * @see UserService#disconnectUser()
     */
    @Test
    public void disconnectUser_shouldCheckThatTheUserIsAuthenticated() throws Exception {
        // given
        securityContext.setPrincipal(null);
        try {
            //when
            userService.disconnectUser();
            Assert.fail("shouldThrowAndErrorIfSessionIdIsNullOrEmpty");
        } catch (CustomException e) {
            //then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * @verifies set user as disconnected
     * @see UserService#disconnectUser()
     */
    @Test
    public void disconnectUser_shouldSetUserAsDisconnected() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);

        // when
        userService.disconnectUser();

        //then
        final UserEntity userEntity = userRepository.findUser(sessionId, username);
        Assertions.assertThat(userEntity.isConnected()).isFalse();
    }

    /**
     * @verifies send a websocket notification
     * @see UserService#disconnectUser()
     */
    @Test
    public void disconnectUser_shouldSendAWebsocketNotification() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);
        // when
        userService.disconnectUser();

        // then
        verify(webSocketSender).sendNotification(principal.getSessionId(), WsTypes.USER_DISCONNECTED, principal.getUsername());
    }

    /**
     * @verifies check that the user is authenticated as admin
     * @see UserService#ban(String)
     */
    @Test
    public void ban_shouldCheckThatTheUserIsAuthenticatedAsAdmin() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(false)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);
        try {
            //when
            userService.ban("username");
            Assert.fail("shouldCheckThatTheUserIsAuthenticatedAsAdmin");
        } catch (CustomException e) {
            //then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
        }
    }

    /**
     * @verifies throw an exception if banned username does not exist in related session
     * @see UserService#ban(String)
     */
    @Test
    public void ban_shouldThrowAnExceptionIfBannedUsernameDoesNotExistInRelatedSession() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        try {
            //when
            userService.ban("invalid_username");
            Assert.fail("shouldThrowAnExceptionIfBannedUsernameDoesNotExistInRelatedSession");
        } catch (CustomException e) {
            //then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
            Assertions.assertThat(e.getMessage()).isEqualTo("Invalid username");
        }
    }

    /**
     * @verifies throw an exception if the session admin try to ban himself
     * @see UserService#ban(String)
     */
    @Test
    public void ban_shouldThrowAnExceptionIfTheSessionAdminTryToBanHimself() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        try {
            //when
            userService.ban(username);
            Assert.fail("shouldThrowAnExceptionIfBannedUsernameIsTheCurrentSessionAdmin");
        } catch (CustomException e) {
            //then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
            Assertions.assertThat(e.getMessage()).isEqualTo("Cannot ban user");
        }
    }

    /**
     * @verifies ban a user even if he is another session admin
     * @see UserService#ban(String)
     */
    @Test
    public void ban_shouldBanAUserEvenIfHeIsAnotherSessionAdmin() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String admin_username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(admin_username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(true)
                .build();
        userRepository.save(connectedUser);

        final String username = "Mike";
        final UserEntity secondUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(true)
                .build();
        userRepository.save(secondUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(admin_username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        // when
        userService.ban(username);

        // then
        final UserEntity userEntity = userRepository.findUser(sessionId, username);
        Assertions.assertThat(userEntity.isConnected()).isFalse();
    }

    /**
     * @verifies set banned user as disconnected
     * @see UserService#ban(String)
     */
    @Test
    public void ban_shouldSetBannedUserAsDisconnected() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String admin_username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(admin_username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(true)
                .build();
        userRepository.save(connectedUser);

        final String username = "Mike";
        final UserEntity secondUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(false)
                .build();
        userRepository.save(secondUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(admin_username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        // when
        userService.ban(username);

        // then
        final UserEntity userEntity = userRepository.findUser(sessionId, username);
        Assertions.assertThat(userEntity.isConnected()).isFalse();
    }

    /**
     * @verifies send a websocket notification
     * @see UserService#ban(String)
     */
    @Test
    public void ban_shouldSendAWebsocketNotification() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String admin_username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(admin_username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(true)
                .build();
        userRepository.save(connectedUser);

        final String username = "Mike";
        final UserEntity secondUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(false)
                .build();
        userRepository.save(secondUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(admin_username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        // when
        userService.ban(username);

        // then
        verify(webSocketSender).sendNotification(principal.getSessionId(), WsTypes.USER_DISCONNECTED, username);
    }

    private void checkJwtToken(String token, String username, String sessionId, UserRole userRole) {
        // then
        final JwtPayload payload = jwtService.parse(token);
        Assertions.assertThat(payload).isNotNull();
        Assertions.assertThat(payload.getUsername()).isEqualTo(username);
        Assertions.assertThat(payload.getSessionId()).isEqualTo(sessionId);
        Assertions.assertThat(payload.getRole()).isEqualTo(userRole.name());
    }
}
