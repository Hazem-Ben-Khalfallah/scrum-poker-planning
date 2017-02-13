package com.blacknebula.scrumpoker.dto;

/**
 * @author hazem
 */
public class VoteCreationDto {
    private String voteId;
    private String storyId;
    private String value;

    public VoteCreationDto() {
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public VoteDto toVoteDto() {
        final VoteDto voteDto = new VoteDto();
        voteDto.setVoteId(this.voteId);
        voteDto.setStoryId(this.storyId);
        voteDto.setValue(this.value);
        return voteDto;
    }
}
