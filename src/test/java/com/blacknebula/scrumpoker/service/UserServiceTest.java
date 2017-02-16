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
import java.util.Optional;
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

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(webSocketSender);
        Mockito.reset(securityContext);
    }

    /**
     * @verifies throw an error if session is null or empty
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldThrowAnErrorIfSessionIsNullOrEmpty() throws Exception {
        try {
            userService.listUsers(null);
            Assert.fail("shouldThrowAnErrorIfSessionIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an error if session does not exist
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldThrowAnErrorIfSessionDoesNotExist() throws Exception {
        try {
            userService.listUsers("invalid_session_id");
            Assert.fail("shouldThrowAnErrorIfSessionIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies return empty list if no user is connected on this session
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldReturnEmptyListIfNoUserIsConnectedOnThisSession() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        // when
        final List<UserDto> connectedUsers = userService.listUsers(sessionId);

        // then
        Assertions.assertThat(connectedUsers).isEmpty();
    }

    /**
     * @verifies return users list if session exists
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldReturnUsersListIfSessionExists() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final List<UserEntity> users = ImmutableList.<UserEntity>builder()
                .add(UserEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withUsername("Leo")
                        .build())
                .add(UserEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withUsername("Leander")
                        .build())
                .build();
        userRepository.save(users);

        // when
        final List<UserDto> connectedUsers = userService.listUsers(sessionId);

        // then
        Assertions.assertThat(connectedUsers).hasSize(2);
    }

    /**
     * @verifies not return disconnected users
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldNotReturnDisconnectedUsers() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final List<UserEntity> users = ImmutableList.<UserEntity>builder()
                .add(UserEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withUsername("Leo")
                        .withConnected(true)
                        .build())
                .add(UserEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withUsername("Leander")
                        .withConnected(false)
                        .build())
                .build();
        userRepository.save(users);

        // when
        final List<UserDto> connectedUsers = userService.listUsers(sessionId);

        // then
        Assertions.assertThat(connectedUsers).hasSize(1);
        Assertions.assertThat(connectedUsers.get(0).getUsername()).isEqualTo("Leo");
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
     * @verifies reconnect user if he was previously disconnected
     * @see UserService#connectUser(UserDto, Consumer)
     */
    @Test
    public void connectUser_shouldReconnectUserIfHeWasPreviouslyDisconnected() throws Exception {
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
                .build();
        userRepository.save(existingDisconnectedUser);

        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId(sessionId)
                .withUsername(username)
                .build();

        // when
        userService.connectUser(userDto, (t) -> {
        });

        // then
        final List<UserEntity> users = userRepository.findUsersBySessionId(userDto.getSessionId());
        Assertions.assertThat(users).hasSize(1);
        Assertions.assertThat(users.get(0).isConnected()).isTrue();
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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.empty());
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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));
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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));
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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        userService.ban(username);

        // then
        verify(webSocketSender).sendNotification(principal.getSessionId(), WsTypes.USER_DISCONNECTED, username);
    }
}
