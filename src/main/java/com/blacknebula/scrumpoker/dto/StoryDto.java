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

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getOrder() {

        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }
}
