package com.influans.sp.repository;

import com.influans.sp.entity.VoteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VoteRepository extends MongoRepository<VoteEntity, String> {
    List<VoteEntity> findBySessionId(String sessionId);

    List<VoteEntity> findByStoryId(String storyId);
}