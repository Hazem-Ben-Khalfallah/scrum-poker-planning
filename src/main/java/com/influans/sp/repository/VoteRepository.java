package com.influans.sp.repository;

import com.influans.sp.entity.VoteEntity;
import com.influans.sp.entity.def.VoteEntityDef;
import com.influans.sp.repository.custom.VoteRepositoryCustom;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VoteRepository extends GenericRepository<VoteEntity, String>, VoteRepositoryCustom {
    @Query(value = "{ " + VoteEntityDef.SESSION_ID + " : ?0 }")
    List<VoteEntity> findBySessionId(String sessionId);

    @Query(value = "{ " + VoteEntityDef.STORY_ID + " : ?0 }")
    List<VoteEntity> findByStoryId(String storyId);
}