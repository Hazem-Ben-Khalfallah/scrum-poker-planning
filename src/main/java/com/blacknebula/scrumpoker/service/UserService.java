package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.UserDto;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.enums.WsTypes;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.JwtService;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.utils.StringUtils;
import com.blacknebula.scrumpoker.websocket.WebSocketSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author hazem
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final WebSocketSender webSocketSender;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public UserService(UserRepository userRepository,
                       SessionRepository sessionRepository,
                       WebSocketSender webSocketSender,
                       JwtService jwtService,
                       AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.webSocketSender = webSocketSender;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }


    /**
     * @return return list of connected user on this session
     * @should check that the user is authenticated
     * @should return connected users only
     */
    public List<UserDto> listUsers() {
        final Principal user = authenticationService.checkAuthenticatedUser();

        final List<UserEntity> users = userRepository.findUsersBySessionId(user.getSessionId());
        return users.stream()
                .map(userEntity -> new UserDto(userEntity.getUserId().getEntityId(),
                        userEntity.getUserId().getSessionId(),
                        userEntity.isAdmin()))
                .collect(Collectors.toList());
    }

    /**
     * @param userDto connected user data
     * @return UserDto with isAdmin attribute set
     * @should throw and error if sessionId is null or empty
     * @should throw and error if username is null or empty
     * @should throw and error if username contains only spaces
     * @should throw and error if sessionId is not valid
     * @should throw an exception if username already used in  the given sessionId with connected status
     * @should create new user if sessionId and username are valid
     * @should reconnect user and generate valid token if he was previously disconnected
     * @should reconnect admin and generate valid token if he has previously logged out
     * @should send a websocket notification
     */
    public UserDto connectUser(UserDto userDto, Consumer<String> connectionConsumer) {
        if (StringUtils.isEmpty(userDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Session should not be null or empty");
        }

        if (StringUtils.isEmpty(userDto.getUsername(), true)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Username should not be null or empty");
        }
        if (!sessionRepository.existsById(userDto.getSessionId())) {
            LOGGER.error("session not found with id = {}", userDto.getSessionId());
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Invalid session id");
        }

        UserEntity userEntity = userRepository.findUser(userDto.getSessionId(), userDto.getUsername());

        if (userEntity == null) {
            userEntity = new UserEntity(userDto.getUsername(), userDto.getSessionId(), false);
            userRepository.save(userEntity);
        } else {
            if (!userEntity.isConnected()) {
                userEntity.setConnected(true);
                userRepository.save(userEntity);
                userDto.setAdmin(userEntity.isAdmin());
            } else {
                LOGGER.error("username {} already used in session {}", userDto.getUsername(), userDto.getSessionId());
                throw new CustomException(CustomErrorCode.DUPLICATE_IDENTIFIER, "Username already used");
            }

        }
        webSocketSender.sendNotification(userDto.getSessionId(), WsTypes.USER_CONNECTED, userDto);
        // generate JWT token
        final UserRole userRole = userEntity.isAdmin() ? UserRole.SESSION_ADMIN : UserRole.VOTER;
        final String token = jwtService.generate(userDto.getSessionId(), userDto.getUsername(), userRole);
        connectionConsumer.accept(token);
        return userDto;
    }

    /**
     * @return empty response
     * @should check that the user is authenticated
     * @should set user as disconnected
     * @should send a websocket notification
     */
    public DefaultResponse disconnectUser() {
        final Principal user = authenticationService.checkAuthenticatedUser();
        final String username = user.getUsername();
        final String sessionId = user.getSessionId();
        disconnectUserInternal(sessionId, username);
        return DefaultResponse.ok();
    }

    /**
     * @return empty response
     * @should check that the user is authenticated as admin
     * @should throw an exception if banned username does not exist in related session
     * @should throw an exception if the session admin try to ban himself
     * @should ban a user even if he is another session admin
     * @should set banned user as disconnected
     * @should send a websocket notification
     */
    public DefaultResponse ban(String username) {
        final Principal user = authenticationService.checkAuthenticatedAdmin();

        if (user.getUsername().equals(username)) {
            LOGGER.error("Cannot ban: User with username {} is admin of session {}", user.getUsername(), user.getSessionId());
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "Cannot ban user");
        }

        final UserEntity userEntity = userRepository.findUser(user.getSessionId(), username);
        if (userEntity == null) {
            LOGGER.error("user not found with username {} in session {}", user.getUsername(), user.getSessionId());
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Invalid username");
        }

        final String sessionId = user.getSessionId();
        disconnectUserInternal(sessionId, username);
        return DefaultResponse.ok();
    }

    public void disconnectUserInternal(String sessionId, String username) {
        final UserEntity userEntity = userRepository.findUser(sessionId, username);

        userEntity.setConnected(false);
        userRepository.save(userEntity);
        webSocketSender.sendNotification(sessionId, WsTypes.USER_DISCONNECTED, username);
    }
}
