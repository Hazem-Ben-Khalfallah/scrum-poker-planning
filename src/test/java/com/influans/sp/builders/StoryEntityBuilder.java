package com.influans.sp.builders;

import com.influans.sp.entity.StoryEntity;

/**
 * @author hazem
 */
public class StoryEntityBuilder {
    private String sessionId;
    private String storyId;

    public static StoryEntityBuilder builder() {
        return new StoryEntityBuilder();
    }

    public StoryEntityBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public StoryEntityBuilder withStoryId(String storyId) {
        this.storyId = storyId;
        return this;
    }

    public StoryEntity build() {
        final StoryEntity storyEntity = new StoryEntity();
        storyEntity.setStoryId(storyId);
        storyEntity.setSessionId(sessionId);
        return storyEntity;
    }
}
