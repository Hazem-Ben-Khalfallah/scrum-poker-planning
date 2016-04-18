package com.influans.sp.repository.impl;

import com.influans.sp.entity.StoryEntity;
import com.influans.sp.repository.custom.StoryRepositoryCustom;

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
