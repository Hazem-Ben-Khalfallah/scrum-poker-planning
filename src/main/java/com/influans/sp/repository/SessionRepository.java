package com.influans.sp.repository;

import com.influans.sp.entity.SessionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SessionRepository extends MongoRepository<SessionEntity, String> {
    SessionEntity findSessionBySessionId(String sessionId);

    List<SessionEntity> findAll();
}