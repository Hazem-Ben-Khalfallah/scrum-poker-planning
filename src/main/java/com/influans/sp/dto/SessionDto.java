package com.influans.sp.dto;

import com.influans.sp.entity.SessionEntity;

import java.util.List;

/**
 * @author hazem
 */
public class SessionDto {
    private String sessionId;
    private String sprintName;
    private String cardSet;
    private List<String> stories;

    public SessionEntity toEntity() {
        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setSessionId(sessionId);
        sessionEntity.setSprintName(sprintName);
        sessionEntity.setCardSet(cardSet);
        sessionEntity.setStories(stories);
        return sessionEntity;
    }

    public String getCardSet() {
        return cardSet;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSprintName() {
        return sprintName;
    }

    public List<String> getStories() {
        return stories;
    }
}
