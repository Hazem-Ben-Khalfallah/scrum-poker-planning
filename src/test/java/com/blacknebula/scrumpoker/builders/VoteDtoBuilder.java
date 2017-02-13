package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.dto.VoteDto;

/**
 * @author hazem
 */
public class VoteDtoBuilder {
    private String voteId;
    private String storyId;
    private String sessionId;
    private String username;
    private String value;


    public static VoteDtoBuilder builder() {
        return new VoteDtoBuilder();
    }

    public VoteDtoBuilder withVoteId(String voteId) {
        this.voteId = voteId;
        return this;
    }

    public VoteDtoBuilder withStoryId(String storyId) {
        this.storyId = storyId;
        return this;
    }

    public VoteDtoBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public VoteDtoBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public VoteDtoBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public VoteDto build() {
        final VoteDto voteDto = new VoteDto();
        voteDto.setVoteId(voteId);
        voteDto.setStoryId(storyId);
        voteDto.setSessionId(sessionId);
        voteDto.setUsername(username);
        voteDto.setValue(value);
        return voteDto;
    }
}
