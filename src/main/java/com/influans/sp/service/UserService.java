package com.influans.sp.service;

import com.influans.sp.dto.UserDto;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.repository.UserRepository;
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

    public List<UserDto> listUsers(String sessionId) {
        final List<UserEntity> users = userRepository.findUsersBySessionId(sessionId);
        return users.stream()
                .map(userEntity -> new UserDto(userEntity.getUserId().getEntityId(),
                        userEntity.getUserId().getSessionId(),
                        userEntity.getIsAdmin())).collect(Collectors.toList());
    }
}
