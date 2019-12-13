package com.blacknebula.scrumpoker.dto;

import com.blacknebula.scrumpoker.entity.StoryEntity;

/**
 * @author hazem
 */
public class StoryDto {
    private String storyId;
    private String sessionId;
    private String storyName;
    private int order;
    private boolean ended;

    public StoryDto() {
    }

    public StoryDto(StoryEntity storyEntity) {
        this.storyId = storyEntity.getStoryId();
        this.storyName = storyEntity.getStoryName();
        this.order = storyEntity.getOrder();
        this.ended = storyEntity.isEnded();
    }

    public String getStoryId() {
        return storyId;
    }

    public StoryDto setStoryId(String storyId) {
        this.storyId = storyId;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public StoryDto setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getStoryName() {
        return storyName;
    }

    public StoryDto setStoryName(String storyName) {
        this.storyName = storyName;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public StoryDto setOrder(int order) {
        this.order = order;
        return this;
    }

    public boolean isEnded() {
        return ended;
    }

    public StoryDto setEnded(boolean ended) {
        this.ended = ended;
        return this;
    }
}
