package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.dto.SessionCreationDto;
import com.blacknebula.scrumpoker.enums.CardSetEnum;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author hazem
 */
public class SessionCreationDtoBuilder {
    private CardSetEnum cardSet;
    private String username;
    private List<String> stories;

    public static SessionCreationDtoBuilder builder() {
        return new SessionCreationDtoBuilder();
    }

    public SessionCreationDtoBuilder withCardSet(CardSetEnum cardSet) {
        this.cardSet = cardSet;
        return this;
    }

    public SessionCreationDtoBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public SessionStoriesBuilder withStories() {
        return new SessionStoriesBuilder(this);
    }

    public SessionCreationDto build() {
        final SessionCreationDto sessionCreationDto = new SessionCreationDto();
        sessionCreationDto.setUsername(username);
        sessionCreationDto.setCardSet(cardSet != null ? cardSet.name() : null);
        sessionCreationDto.setStories(stories);
        return sessionCreationDto;
    }

    private void setStories(List<String> stories) {
        this.stories = stories;
    }

    public class SessionStoriesBuilder {
        private ImmutableList.Builder<String> builder = ImmutableList.builder();
        private SessionCreationDtoBuilder sessionDtoBuilder;

        public SessionStoriesBuilder(SessionCreationDtoBuilder sessionDtoBuilder) {
            this.sessionDtoBuilder = sessionDtoBuilder;
        }

        public SessionStoriesBuilder addStory(String story) {
            builder.add(story);
            return this;
        }

        public SessionCreationDtoBuilder collect() {
            sessionDtoBuilder.setStories(builder.build());
            return sessionDtoBuilder;
        }
    }
}
