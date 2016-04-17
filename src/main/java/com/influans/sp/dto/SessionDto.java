package com.influans.sp.dto;

import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.enums.CardSetEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hazem
 */
public class SessionDto {
    private String sessionId;
    private String username;
    private String sprintName;
    private String cardSet;
    private List<String> stories = new ArrayList<>();

    public SessionDto() {
    }

    public SessionDto(SessionEntity sessionEntity) {
        this.sessionId = sessionEntity.getSessionId();
        this.sprintName = sessionEntity.getSprintName();
        this.cardSet = sessionEntity.getCardSet().getValue();
    }

    public SessionEntity toEntity() {
        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setSessionId(sessionId);
        sessionEntity.setSprintName(sprintName);
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
}
