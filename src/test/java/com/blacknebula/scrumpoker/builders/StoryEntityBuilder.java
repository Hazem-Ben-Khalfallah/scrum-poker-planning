package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.entity.StoryEntity;

/**
 * @author hazem
 */
public class StoryEntityBuilder {
    private String sessionId;
    private String storyId;
    private boolean ended;

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

    public StoryEntityBuilder withEnded(Boolean ended) {
        this.ended = ended;
        return this;
    }

    public StoryEntity build() {
        final StoryEntity storyEntity = new StoryEntity();
        storyEntity.setStoryId(storyId);
        storyEntity.setSessionId(sessionId);
        storyEntity.setEnded(ended);
        return storyEntity;
    }
}
