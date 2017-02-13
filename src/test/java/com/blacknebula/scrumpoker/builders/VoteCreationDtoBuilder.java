package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.dto.VoteCreationDto;

/**
 * @author hazem
 */
public class VoteCreationDtoBuilder {
    private String voteId;
    private String storyId;
    private String value;


    public static VoteCreationDtoBuilder builder() {
        return new VoteCreationDtoBuilder();
    }

    public VoteCreationDtoBuilder withVoteId(String voteId) {
        this.voteId = voteId;
        return this;
    }

    public VoteCreationDtoBuilder withStoryId(String storyId) {
        this.storyId = storyId;
        return this;
    }

    public VoteCreationDtoBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public VoteCreationDto build() {
        final VoteCreationDto voteCreationDto = new VoteCreationDto();
        voteCreationDto.setVoteId(voteId);
        voteCreationDto.setStoryId(storyId);
        voteCreationDto.setValue(value);
        return voteCreationDto;
    }
}
