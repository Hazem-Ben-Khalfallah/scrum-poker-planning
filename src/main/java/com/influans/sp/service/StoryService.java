package com.influans.sp.service;

import com.google.common.collect.ImmutableMap;
import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.StoryDto;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.def.StoryEntityDef;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.DAOResponse;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hazem
 */
@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private SessionRepository sessionRepository;

    public List<StoryDto> listStories(String sessionId) {
        final List<StoryDto> stories = new ArrayList<>();
        storyRepository.findBySessionId(sessionId).forEach(storyEntity -> //
                stories.add(new StoryDto(storyEntity)));
        return stories;
    }

    public DefaultResponse delete(String storyId) {
        if (StringUtils.isEmpty(storyId) || !storyRepository.exists(storyId)) {
            return DefaultResponse.ko();
        }

        storyRepository.delete(storyId);
        return DefaultResponse.ok();
    }

    public StoryDto createStory(StoryDto storyDto) {
        if (StringUtils.isEmpty(storyDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "session should not be null or empty");
        }
        if (!sessionRepository.exists(storyDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found");
        }

        final StoryEntity storyEntity = new StoryEntity(storyDto.getSessionId(), storyDto.getStoryName(), storyDto.getOrder());
        storyRepository.save(storyEntity);
        storyDto.setStoryId(storyEntity.getStoryId());
        return storyDto;
    }

    public DefaultResponse endStory(String storyId) {
        final DAOResponse daoResponse = storyRepository.update(storyId, ImmutableMap.<String, Object>builder()
                .put(StoryEntityDef.ENDED, true)
                .build());
        if (daoResponse.getnAffected() > 0) {
            return DefaultResponse.ok();
        }
        return DefaultResponse.ko();
    }
}
