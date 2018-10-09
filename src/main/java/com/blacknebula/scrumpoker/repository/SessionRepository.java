package com.blacknebula.scrumpoker.repository;

import com.blacknebula.scrumpoker.entity.SessionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SessionRepository extends MongoRepository<SessionEntity, String> {
}