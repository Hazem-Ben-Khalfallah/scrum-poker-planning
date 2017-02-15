package com.blacknebula.scrumpoker.dto;

import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.enums.CardSetEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hazem
 */
public class SessionCreationDto {
    private String sessionId;
    private String username;
    private String storyNamePrefix;
    private String cardTheme;
    private String cardSet;
    private List<String> stories = new ArrayList<>();

    public SessionCreationDto() {
    }

    public SessionEntity toEntity() {
        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setSessionId(sessionId);
        sessionEntity.setStoryNamePrefix(storyNamePrefix);
        sessionEntity.setCardTheme(cardTheme);
        sessionEntity.setCardSet(CardSetEnum.toEnum(cardSet));
        return sessionEntity;
    }

    public List<StoryEntity> toStories(String sessionId) {
        final List<StoryEntity> storyEntities = new ArrayList<>();
        int order = 1;
        for (String story : stories) {
            storyEntities.add(new StoryEntity(sessionId, story, order));
            order++;
        }
        return storyEntities;
    }

    public String getCardSet() {
        return cardSet;
    }

    public void setCardSet(String cardSet) {
        this.cardSet = cardSet;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getStories() {
        return stories;
    }

    public void setStories(List<String> stories) {
        this.stories = stories;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStoryNamePrefix() {
        return storyNamePrefix;
    }

    public void setStoryNamePrefix(String storyNamePrefix) {
        this.storyNamePrefix = storyNamePrefix;
    }

    public String getCardTheme() {
        return cardTheme;
    }

    public void setCardTheme(String cardTheme) {
        this.cardTheme = cardTheme;
    }
}
