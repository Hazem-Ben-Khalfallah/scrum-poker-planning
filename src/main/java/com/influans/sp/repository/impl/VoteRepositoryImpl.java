package com.influans.sp.repository.impl;

import com.influans.sp.entity.VoteEntity;
import com.influans.sp.repository.custom.VoteRepositoryCustom;

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
}
