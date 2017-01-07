package com.influans.sp.repository.custom;

import com.influans.sp.entity.VoteEntity;

/**
 * @author hazem
 */
public interface VoteRepositoryCustom extends GenericRepositoryCustom<VoteEntity, String> {
    VoteEntity getVoteByUserOnStory(String username, String storyId);
}
