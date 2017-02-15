package com.blacknebula.scrumpoker.entity;

import com.blacknebula.scrumpoker.entity.def.SessionEntityDef;
import com.blacknebula.scrumpoker.enums.CardSetEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "session")
public class SessionEntity {
    @Id
    private String sessionId;
    @Field(SessionEntityDef.SPRINT_NAME)
    private String sprintName;
    @Field(SessionEntityDef.SPRINT_NAME_PREFIX)
    private String storyNamePrefix;
    @Field(SessionEntityDef.CARD_THEME)
    private String cardTheme;
    @Field(SessionEntityDef.CARD_SET)
    private CardSetEnum cardSet;

    public SessionEntity() {
    }

    public SessionEntity(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public CardSetEnum getCardSet() {
        return cardSet;
    }

    public void setCardSet(CardSetEnum cardSet) {
        this.cardSet = cardSet;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
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