package com.blacknebula.scrumpoker.dto;

/**
 * @author hazem
 */
public class StoryCreationDto {
    private String storyId;
    private String storyName;
    private int order;

    public StoryCreationDto() {
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
