package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.dto.StoryDto;

/**
 * @author hazem
 */
public class StoryDtoBuilder {

    private String sessionId;
    private String storyName;
    private int order;

    public static StoryDtoBuilder builder() {
        return new StoryDtoBuilder();
    }

    public StoryDtoBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public StoryDtoBuilder withStoryName(String storyName) {
        this.storyName = storyName;
        return this;
    }

    public StoryDtoBuilder withOrder(int order) {
        this.order = order;
        return this;
    }

    public StoryDto build() {
        final StoryDto storyDto = new StoryDto();
        storyDto.setSessionId(sessionId);
        storyDto.setStoryName(storyName);
        storyDto.setOrder(order);
        return storyDto;
    }
}
