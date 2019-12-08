package com.blacknebula.scrumpoker.builders;

import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.enums.CardSetEnum;

/**
 * @author hazem
 */
public class SessionEntityBuilder {
    private String sessionId;
    private CardSetEnum cardSet;

    public static SessionEntityBuilder builder() {
        return new SessionEntityBuilder();
    }

    public SessionEntityBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public SessionEntityBuilder withCardSet(CardSetEnum cardSet) {
        this.cardSet = cardSet;
        return this;
    }

    public SessionEntity build() {
        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setSessionId(sessionId);
        sessionEntity.setCardSet(cardSet);
        return sessionEntity;
    }
}
