package com.influans.sp.repository.custom;

import com.influans.sp.entity.VoteEntity;

/**
 * @author hazem
 */
public interface VoteRepositoryCustom extends GenericRepositoryCustom<VoteEntity, String> {
    boolean hasVoted(String username, String storyId);
}
