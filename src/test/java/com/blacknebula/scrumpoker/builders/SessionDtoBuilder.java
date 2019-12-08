package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.dto.SessionDto;
import com.blacknebula.scrumpoker.enums.CardSetEnum;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author hazem
 */
public class SessionDtoBuilder {
    private String sessionId;
    private CardSetEnum cardSet;
    private String username;
    private String sprintName;
    private List<String> stories;

    public static SessionDtoBuilder builder() {
        return new SessionDtoBuilder();
    }

    public SessionDtoBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public SessionDtoBuilder withCardSet(CardSetEnum cardSet) {
        this.cardSet = cardSet;
        return this;
    }

    public SessionDtoBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public SessionDtoBuilder withSprintName(String sprintName) {
        this.sprintName = sprintName;
        return this;
    }

    public SessionStoriesBuilder withStories() {
        return new SessionStoriesBuilder(this);
    }

    public SessionDto build() {
        final SessionDto sessionDto = new SessionDto();
        sessionDto.setSessionId(sessionId);
        sessionDto.setUsername(username);
        sessionDto.setSprintName(sprintName);
        sessionDto.setCardSet(cardSet != null ? cardSet.name() : null);
        sessionDto.setStories(stories);
        return sessionDto;
    }

    private void setStories(List<String> stories) {
        this.stories = stories;
    }

    public class SessionStoriesBuilder {
        private ImmutableList.Builder<String> builder = ImmutableList.builder();
        private SessionDtoBuilder sessionDtoBuilder;

        public SessionStoriesBuilder(SessionDtoBuilder sessionDtoBuilder) {
            this.sessionDtoBuilder = sessionDtoBuilder;
        }

        public SessionStoriesBuilder addStory(String story) {
            builder.add(story);
            return this;
        }

        public SessionDtoBuilder collect() {
            sessionDtoBuilder.setStories(builder.build());
            return sessionDtoBuilder;
        }
    }
}
