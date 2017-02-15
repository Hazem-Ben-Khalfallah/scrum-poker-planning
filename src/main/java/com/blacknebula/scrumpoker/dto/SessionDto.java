package com.blacknebula.scrumpoker.dto;

import com.blacknebula.scrumpoker.entity.SessionEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hazem
 */
public class SessionDto {
    private String sessionId;
    private String username;
    private String sprintName;
    private String storyNamePrefix;
    private String cardTheme;
    private String cardSet;
    private List<String> stories = new ArrayList<>();

    public SessionDto() {
    }

    public SessionDto(SessionEntity sessionEntity) {
        this.sessionId = sessionEntity.getSessionId();
        this.sprintName = sessionEntity.getSprintName();
        this.storyNamePrefix = sessionEntity.getStoryNamePrefix();
        this.cardTheme = sessionEntity.getCardTheme();
        this.cardSet = sessionEntity.getCardSet() != null ? sessionEntity.getCardSet().getValue() : null;
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

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
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
