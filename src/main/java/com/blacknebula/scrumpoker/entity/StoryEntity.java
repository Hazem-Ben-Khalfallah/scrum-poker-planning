package com.blacknebula.scrumpoker.entity;

import com.blacknebula.scrumpoker.entity.def.StoryEntityDef;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "story")
public class StoryEntity {
    @Id
    private String storyId;
    @Field(StoryEntityDef.STORY_NAME)
    private String storyName;
    @Field(StoryEntityDef.SESSION_ID)
    private String sessionId;
    @Field(StoryEntityDef.ORDER)
    private int order;
    @Field(StoryEntityDef.ENDED)
    private boolean ended;

    public StoryEntity() {
    }

    public StoryEntity(String sessionId, String storyName, int order) {
        this.sessionId = sessionId;
        this.storyName = storyName;
        this.order = order;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }
}