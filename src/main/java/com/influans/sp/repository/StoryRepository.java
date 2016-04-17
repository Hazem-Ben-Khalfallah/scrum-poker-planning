package com.influans.sp.repository;

import com.influans.sp.entity.StoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StoryRepository extends MongoRepository<StoryEntity, String> {
    List<StoryEntity> findBySessionId(String sessionId);
}