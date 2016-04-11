package com.influans.sp.repository.custom;

import com.influans.sp.entity.UserEntity;

import java.util.List;

/**
 * @author hazem
 */
public interface UserRepositoryCustom {
    List<UserEntity> findUsersBySessionId(String sessionId);
}
