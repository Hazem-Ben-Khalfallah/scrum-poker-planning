package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.dto.StoryCreationDto;

/**
 * @author hazem
 */
public class StoryCreationDtoBuilder {

    private String storyName;
    private int order;

    public static StoryCreationDtoBuilder builder() {
        return new StoryCreationDtoBuilder();
    }

    public StoryCreationDtoBuilder withStoryName(String storyName) {
        this.storyName = storyName;
        return this;
    }

    public StoryCreationDtoBuilder withOrder(int order) {
        this.order = order;
        return this;
    }

    public StoryCreationDto build() {
        final StoryCreationDto storyDto = new StoryCreationDto();
        storyDto.setStoryName(storyName);
        storyDto.setOrder(order);
        return storyDto;
    }
}
