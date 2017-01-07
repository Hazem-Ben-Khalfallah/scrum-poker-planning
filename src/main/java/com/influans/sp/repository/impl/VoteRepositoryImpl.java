package com.influans.sp.repository.impl;

import com.influans.sp.entity.VoteEntity;
import com.influans.sp.entity.def.VoteEntityDef;
import com.influans.sp.repository.custom.VoteRepositoryCustom;
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
    public boolean hasVoted(String username, String storyId) {
        final Query q = new Query();
        q.addCriteria(Criteria.where(VoteEntityDef.STORY_ID).is(storyId)
                .andOperator(Criteria.where(VoteEntityDef.USERNAME).is(username)));
        return mongoTemplate.exists(q, getTClass());
    }
}
