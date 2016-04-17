package com.influans.sp.entity;

import com.influans.sp.entity.def.SessionEntityDef;
import com.influans.sp.enums.CardSetEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "session")
public class SessionEntity {
    @Id
    private String sessionId;
    @Field(SessionEntityDef.SPRINT_NAME)
    private String sprintName;
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
}