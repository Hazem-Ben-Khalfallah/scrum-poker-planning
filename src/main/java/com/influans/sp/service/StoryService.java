package com.influans.sp.service;

import com.google.common.collect.ImmutableMap;
import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.StoryDto;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.def.StoryEntityDef;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.DAOResponse;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.utils.StringUtils;
import com.influans.sp.websocket.WebSocketSender;
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
    @Autowired
    private WebSocketSender webSocketSender;

    /**
     * @should return stories related to the given session
     * @should throw an exception if session id is null or empty
     * @should throw an exception if session id is not valid
     * @param sessionId session id
     * @return list of stories
     */
    public List<StoryDto> listStories(String sessionId) {
        final List<StoryDto> stories = new ArrayList<>();
        storyRepository.findBySessionId(sessionId).forEach(storyEntity -> //
                stories.add(new StoryDto(storyEntity)));
        return stories;
    }

    /**
     * @should delete a story
     * @should throw an exception if storyId is null or empty
     * @param storyId story id
     * @return empty response
     */
    public DefaultResponse delete(String storyId) {
        final StoryEntity storyEntity = storyRepository.findOne(storyId);
        if (StringUtils.isEmpty(storyId) || storyEntity == null) {
            return DefaultResponse.ko();
        }

        storyRepository.delete(storyId);
        webSocketSender.sendNotification(storyEntity.getSessionId(), WsTypes.STORY_REMOVED, storyId);
        return DefaultResponse.ok();
    }

    /**
     * @should throw an exception if sessionId is empty or null
     * @should throw an exception if session does not exist
     * @should create a story related to the given sessionId
     * @param storyDto storyDto
     * @return StoryDto with new id
     */
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
        webSocketSender.sendNotification(storyEntity.getSessionId(), WsTypes.STORY_ADDED, storyDto);
        return storyDto;
    }

    /**
     * @should throw an exception if storyId is empty or null
     * @should throw an exception if story does not exist
     * @should set story as ended
     * @param storyId storyId
     * @return empty response
     */
    public DefaultResponse endStory(String storyId) {
        final StoryEntity storyEntity = storyRepository.findOne(storyId);
        if (StringUtils.isEmpty(storyId) || storyEntity == null) {
            return DefaultResponse.ko();
        }
        final DAOResponse daoResponse = storyRepository.update(storyId, ImmutableMap.<String, Object>builder()
                .put(StoryEntityDef.ENDED, true)
                .build());

        if (daoResponse.getnAffected() > 0) {
            webSocketSender.sendNotification(storyEntity.getSessionId(), WsTypes.STORY_ENDED, storyId);
            return DefaultResponse.ok();
        }
        return DefaultResponse.ko();
    }
}
