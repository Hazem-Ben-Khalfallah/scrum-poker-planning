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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private WebSocketSender webSocketSender;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;


    /**
     * @param sessionId session id
     * @return return list of connected user on this session
     * @should return users list if session exists
     * @should throw an error if session is null or empty
     * @should throw an error if session does not exist
     * @should return empty list if no user is connected on this session
     * @should not return disconnected users
     */
    public List<UserDto> listUsers(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "session should not be null or empty");
        }
        if (!sessionRepository.exists(sessionId)) {
            LOGGER.error("session not found with id = {}", sessionId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found");
        }
        final List<UserEntity> users = userRepository.findUsersBySessionId(sessionId);
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
     * @should reconnect user if he was previously disconnected
     * @should send a websocket notification
     */
    public DefaultResponse connectUser(UserDto userDto, Consumer<String> connectionConsumer) {
        if (StringUtils.isEmpty(userDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Session should not be null or empty");
        }

        if (StringUtils.isEmpty(userDto.getUsername(), true)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Username should not be null or empty");
        }
        if (!sessionRepository.exists(userDto.getSessionId())) {
            LOGGER.error("session not found with id = " + userDto.getSessionId());
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
            } else {
                LOGGER.error("username {} already used in session {}", userDto.getUsername(), userDto.getSessionId());
                throw new CustomException(CustomErrorCode.DUPLICATE_IDENTIFIER, "Username already used");
            }

        }
        webSocketSender.sendNotification(userDto.getSessionId(), WsTypes.USER_CONNECTED, userDto);
        // generate JWT token
        final String token = jwtService.generate(userDto.getSessionId(), userDto.getUsername(), UserRole.VOTER);
        connectionConsumer.accept(token);
        return DefaultResponse.ok();
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

    public void disconnectUserInternal(String sessionId, String username) {
        final UserEntity userEntity = userRepository.findUser(sessionId, username);

        userEntity.setConnected(false);
        userRepository.save(userEntity);
        webSocketSender.sendNotification(sessionId, WsTypes.USER_DISCONNECTED, username);
    }
}
