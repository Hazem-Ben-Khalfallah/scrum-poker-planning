package com.blacknebula.scrumpoker.entity;

import com.blacknebula.scrumpoker.entity.def.EntityIdDef;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

public class EntityId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Field(EntityIdDef.ENTITY_ID)
    private String entityId;

    @Field(EntityIdDef.SESSION_ID)
    @Indexed
    private String sessionId;

    public EntityId() {

    }

    public EntityId(String entityId, String sessionId) {
        this.entityId = entityId;
        this.sessionId = sessionId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
