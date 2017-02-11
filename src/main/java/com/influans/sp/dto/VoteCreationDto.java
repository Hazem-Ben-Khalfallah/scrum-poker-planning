package com.influans.sp.dto;

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
}
