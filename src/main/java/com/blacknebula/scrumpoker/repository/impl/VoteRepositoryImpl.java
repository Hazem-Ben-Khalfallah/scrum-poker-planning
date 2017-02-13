package com.blacknebula.scrumpoker.repository.impl;

import com.blacknebula.scrumpoker.entity.VoteEntity;
import com.blacknebula.scrumpoker.entity.def.VoteEntityDef;
import com.blacknebula.scrumpoker.repository.custom.VoteRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author hazem
 */
public class VoteRepositoryImpl extends GenericRepositoryImpl<VoteEntity, String> implements VoteRepositoryCustom {
    @Override
    public Class<VoteEntity> getTClass() {
        return VoteEntity.class;
    }

    @Override
    public String getId(VoteEntity voteEntity) {
        return voteEntity.getVoteId();
    }

    @Override
    public VoteEntity getVoteByUserOnStory(String username, String storyId) {
        final Query q = new Query();
        q.addCriteria(Criteria.where(VoteEntityDef.STORY_ID).is(storyId)
                .andOperator(Criteria.where(VoteEntityDef.USERNAME).is(username)));
        return mongoTemplate.findOne(q, getTClass());
    }
}
