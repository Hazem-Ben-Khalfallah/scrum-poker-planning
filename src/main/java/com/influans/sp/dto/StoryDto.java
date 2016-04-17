package com.influans.sp.dto;

/**
 * @author hazem
 */
public class StoryDto {
    private String storyId;
    private String sessionId;
    private String storyName;
    private int order;

    public StoryDto() {
    }

    public StoryDto(String storyId, String storyName, int order) {
        this.storyId = storyId;
        this.storyName = storyName;
        this.order = order;
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
