package com.influans.sp.service;

import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.UserDto;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.utils.StringUtils;
import com.influans.sp.websocket.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hazem
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private WebSocketSender webSocketSender;


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
     * @should reconnect user if it was previously disconnected
     */
    public UserDto connectUser(UserDto userDto) {
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
        return userDto;
    }

    /**
     * @param userDto connected use
     * @return empty response
     * @should throw and error if sessionId is null or empty
     * @should throw and error if username is null or empty
     * @should throw an error if user was not found
     * @should throw an error if sessions is not found
     * @should set user as disconnected
     */
    public DefaultResponse disconnectUser(UserDto userDto) {
        if (StringUtils.isEmpty(userDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "session should not be null or empty");
        }

        if (StringUtils.isEmpty(userDto.getUsername(), true)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "username should not be null or empty");
        }
        if (!sessionRepository.exists(userDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found with id = " + userDto.getSessionId());
        }

        final UserEntity userEntity = userRepository.findUser(userDto.getSessionId(), userDto.getUsername());

        if (userEntity == null) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "user not found with username = " + userDto.getUsername());
        }

        userEntity.setConnected(false);
        userRepository.save(userEntity);
        webSocketSender.sendNotification(userDto.getSessionId(), WsTypes.USER_DISCONNECTED, userDto.getUsername());
        return DefaultResponse.ok();
    }
}
