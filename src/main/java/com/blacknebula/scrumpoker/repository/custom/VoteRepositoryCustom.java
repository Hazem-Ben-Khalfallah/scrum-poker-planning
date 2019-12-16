package com.blacknebula.scrumpoker.repository.custom;

import com.blacknebula.scrumpoker.entity.VoteEntity;

/**
 * @author hazem
 */
public interface VoteRepositoryCustom extends GenericRepositoryCustom<VoteEntity, String> {

    /**
     * Should return unique user vote on a given story
     *
     * @param username username
     * @param storyId  storyID
     * @return VoteEntity
     * @should return a user vote on a given story
     * @should return null if username is invalid
     * @should return null if storyId is invalid
     */
    VoteEntity getVoteByUserOnStory(String username, String storyId);
}
