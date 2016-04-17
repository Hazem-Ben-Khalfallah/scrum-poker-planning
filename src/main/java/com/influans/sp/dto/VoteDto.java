package com.influans.sp.dto;

/**
 * @author hazem
 */
public class VoteDto {
    private String voteId;
    private String storyId;
    private String sessionId;
    private String username;
    private String value;

    public VoteDto() {
    }

    public VoteDto(String voteId, String sessionId, String storyId, String username, String value) {
        this.voteId = voteId;
        this.sessionId = sessionId;
        this.storyId = storyId;
        this.username = username;
        this.value = value;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
