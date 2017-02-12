package com.influans.sp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        VoteDto voteDto = (VoteDto) o;

        return new EqualsBuilder()
                .append(voteId, voteDto.voteId)
                .append(storyId, voteDto.storyId)
                .append(sessionId, voteDto.sessionId)
                .append(username, voteDto.username)
                .append(value, voteDto.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(voteId)
                .append(storyId)
                .append(sessionId)
                .append(username)
                .append(value)
                .toHashCode();
    }
}
