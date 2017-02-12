package com.influans.sp.service;

import com.google.common.collect.ImmutableList;
import com.influans.sp.ApplicationTest;
import com.influans.sp.builders.PrincipalBuilder;
import com.influans.sp.builders.SessionEntityBuilder;
import com.influans.sp.builders.UserDtoBuilder;
import com.influans.sp.builders.UserEntityBuilder;
import com.influans.sp.dto.UserDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.enums.UserRole;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.security.Principal;
import com.influans.sp.security.SecurityContext;
import com.influans.sp.websocket.WebSocketSender;
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
     * @see UserService#connectUser(com.influans.sp.dto.UserDto, Consumer)
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
        }
    }

    /**
     * @verifies throw and error if username is null or empty
     * @see UserService#connectUser(com.influans.sp.dto.UserDto, Consumer)
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
     * @see UserService#connectUser(com.influans.sp.dto.UserDto, Consumer)
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
        }
    }

    /**
     * @verifies create new user if withSessionId and withUsername are valid
     * @see UserService#connectUser(com.influans.sp.dto.UserDto, Consumer)
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
     * @see UserService#connectUser(com.influans.sp.dto.UserDto, Consumer)
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
        }
    }

    /**
     * @verifies reconnect user if he was previously disconnected
     * @see UserService#connectUser(com.influans.sp.dto.UserDto, Consumer)
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
}
