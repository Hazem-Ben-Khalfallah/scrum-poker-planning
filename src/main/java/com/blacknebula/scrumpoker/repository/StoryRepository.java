package com.blacknebula.scrumpoker.repository;

import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.repository.custom.StoryRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StoryRepository extends MongoRepository<StoryEntity, String>, StoryRepositoryCustom {
    List<StoryEntity> findBySessionId(String sessionId);
}