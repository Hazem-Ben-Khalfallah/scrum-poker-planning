package com.blacknebula.scrumpoker.repository;

import com.blacknebula.scrumpoker.entity.SessionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<SessionEntity, String> {
}