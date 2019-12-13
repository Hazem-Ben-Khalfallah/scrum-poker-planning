package com.blacknebula.scrumpoker.dto;

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

    public VoteDto setVoteId(String voteId) {
        this.voteId = voteId;
        return this;
    }

    public String getStoryId() {
        return storyId;
    }

    public VoteDto setStoryId(String storyId) {
        this.storyId = storyId;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public VoteDto setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public VoteDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getValue() {
        return value;
    }

    public VoteDto setValue(String value) {
        this.value = value;
        return this;
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
