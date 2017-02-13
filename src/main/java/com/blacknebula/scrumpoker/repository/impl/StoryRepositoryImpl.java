package com.blacknebula.scrumpoker.repository.impl;

import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.repository.custom.StoryRepositoryCustom;

/**
 * @author hazem
 */
public class StoryRepositoryImpl extends GenericRepositoryImpl<StoryEntity, String> implements StoryRepositoryCustom {
    @Override
    public Class<StoryEntity> getTClass() {
        return StoryEntity.class;
    }

    @Override
    public String getId(StoryEntity storyEntity) {
        return storyEntity.getStoryId();
    }
}
