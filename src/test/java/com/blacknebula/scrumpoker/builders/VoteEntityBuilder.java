package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.entity.VoteEntity;

/**
 * @author hazem
 */
public class VoteEntityBuilder {

    private String voteId;
    private String storyId;
    private String username;
    private String sessionId;
    private String value;

    public static VoteEntityBuilder builder() {
        return new VoteEntityBuilder();
    }


    public VoteEntityBuilder withVoteId(String voteId) {
        this.voteId = voteId;
        return this;
    }

    public VoteEntityBuilder withStoryId(String storyId) {
        this.storyId = storyId;
        return this;
    }

    public VoteEntityBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public VoteEntityBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public VoteEntityBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public VoteEntity build() {
        final VoteEntity voteEntity = new VoteEntity();
        voteEntity.setVoteId(voteId);
        voteEntity.setStoryId(storyId);
        voteEntity.setUsername(username);
        voteEntity.setSessionId(sessionId);
        voteEntity.setValue(value);
        return voteEntity;
    }
}
