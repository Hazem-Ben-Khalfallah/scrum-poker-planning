package com.influans.sp.service;

import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.UserDto;
import com.influans.sp.entity.EntityId;
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

    public List<UserDto> listUsers(String sessionId) {
        final List<UserEntity> users = userRepository.findUsersBySessionId(sessionId);
        return users.stream()
                .map(userEntity -> new UserDto(userEntity.getUserId().getEntityId(),
                        userEntity.getUserId().getSessionId(),
                        userEntity.getIsAdmin())).collect(Collectors.toList());
    }

    public UserDto connectUser(UserDto userDto) {
        if (StringUtils.isEmpty(userDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "session should not be null or empty");
        }
        if (!sessionRepository.exists(userDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found");
        }

        UserEntity userEntity = userRepository.findUser(userDto.getSessionId(), userDto.getUsername());

        if (userEntity == null) {
            userEntity = new UserEntity(userDto.getUsername(), userDto.getSessionId(), false);
            userRepository.save(userEntity);
        }
        webSocketSender.sendNotification(userDto.getSessionId(), WsTypes.USER_CONNECTED, userDto);
        return userDto;
    }

    public DefaultResponse disconnectUser(UserDto userDto) {
        final UserEntity userEntity = userRepository.findOne(new EntityId(userDto.getUsername(), userDto.getSessionId()));
        if (userEntity == null) {
            return DefaultResponse.ko();
        }

        webSocketSender.sendNotification(userDto.getSessionId(), WsTypes.USER_DISCONNECTED, userDto.getUsername());
        return DefaultResponse.ok();
    }
}
