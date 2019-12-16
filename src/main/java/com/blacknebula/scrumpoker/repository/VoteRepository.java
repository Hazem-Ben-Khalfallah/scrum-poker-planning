package com.blacknebula.scrumpoker.repository;

import com.blacknebula.scrumpoker.entity.VoteEntity;
import com.blacknebula.scrumpoker.entity.def.VoteEntityDef;
import com.blacknebula.scrumpoker.repository.custom.VoteRepositoryCustom;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VoteRepository extends GenericRepository<VoteEntity, String>, VoteRepositoryCustom {

    @Query(value = "{ " + VoteEntityDef.STORY_ID + " : ?0 }")
    List<VoteEntity> findByStoryId(String storyId);
}