package com.influans.sp.service;

import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.UserDto;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.enums.UserRole;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.security.JwtService;
import com.influans.sp.security.Principal;
import com.influans.sp.security.SecurityContext;
import com.influans.sp.utils.StringUtils;
import com.influans.sp.websocket.WebSocketSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    private SecurityContext securityContext;
    @Autowired
    private JwtService jwtService;


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
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found with id = " + sessionId);
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
            throw new CustomException(CustomErrorCode.BAD_ARGS, "session should not be null or empty");
        }

        if (StringUtils.isEmpty(userDto.getUsername(), true)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "username should not be null or empty");
        }
        if (!sessionRepository.exists(userDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found with id = " + userDto.getSessionId());
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
                throw new CustomException(CustomErrorCode.DUPLICATE_IDENTIFIER, String.format("username %s already used in session %s", userDto.getUsername(), userDto.getSessionId()));
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
     * @should throw and error if sessionId is null or empty
     * @should throw and error if username is null or empty
     * @should throw an error if user was not found
     * @should throw an error if sessions is not found
     * @should set user as disconnected
     * @should send a websocket notification
     */
    public DefaultResponse disconnectUser() {
        final Optional<Principal> optional = securityContext.getAuthenticationContext();

        if (!optional.isPresent()) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated");
        }

        final Principal user = optional.get();

        if (StringUtils.isEmpty(user.getSessionId())) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "session should not be null or empty");
        }

        if (StringUtils.isEmpty(user.getUsername(), true)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "username should not be null or empty");
        }
        if (!sessionRepository.exists(user.getSessionId())) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "session not found with id = " + user.getSessionId());
        }

        final UserEntity userEntity = userRepository.findUser(user.getSessionId(), user.getUsername());

        if (userEntity == null) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "user not found with username = " + user.getUsername());
        }

        userEntity.setConnected(false);
        userRepository.save(userEntity);
        webSocketSender.sendNotification(user.getSessionId(), WsTypes.USER_DISCONNECTED, user.getUsername());
        LOGGER.info("[SECURITY] Principal.username: {} , user.username: {}, equal: {}", user.getUsername(), user.getUsername(), user.getUsername().equals(user.getUsername()));
        return DefaultResponse.ok();
    }
}
