package com.blacknebula.scrumpoker.repository.custom;

import com.blacknebula.scrumpoker.entity.UserEntity;

import java.util.List;

/**
 * @author hazem
 */
public interface UserRepositoryCustom {
    /**
     * Find users connected to a given session
     *
     * @param sessionId sessionId
     * @return list of UserEntity
     * @should return users list related to a given session
     * @should return empty list if sessionId is invalid
     */
    List<UserEntity> findUsersBySessionId(String sessionId);

    /**
     * Find unique user by sessionId and userName
     *
     * @param sessionId sessionId
     * @param username  username
     * @return UserEntity
     * @should return user connected to a given session with a given username
     * @should return null if sessionId is invalid
     * @should return null if username is invalid
     */
    UserEntity findUser(String sessionId, String username);
}
