package com.influans.sp.service;

import com.google.common.collect.ImmutableMap;
import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.StoryDto;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.def.StoryEntityDef;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.utils.StringUtils;
import com.influans.sp.websocket.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * @param sessionId session id
     * @return list of stories
     * @should return stories related to the given session
     * @should throw an exception if session id is null or empty
     * @should throw an exception if session id is not valid
     */
    public List<StoryDto> listStories(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "sessionId should not be null or empty");
        }
        if (!sessionRepository.exists(sessionId)) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found with id = " + sessionId);
        }

        final List<StoryDto> stories = new ArrayList<>();
        storyRepository.findBySessionId(sessionId).forEach(storyEntity -> //
                stories.add(new StoryDto(storyEntity)));
        return stories;
    }

    /**
     * @param storyId story id
     * @return empty response
     * @should delete a story
     * @should throw an exception if storyId is null or empty
     * @should throw an exception if story does not exist
     * @should throw an exception if user is not admin of the related session
     * @should send a websocket notification
     */
    public DefaultResponse delete(String storyId) {
        if (StringUtils.isEmpty(storyId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }

        final StoryEntity storyEntity = storyRepository.findOne(storyId);
        if (Objects.isNull(storyEntity)) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "story not found with id = " + storyId);
        }

        storyRepository.delete(storyId);
        webSocketSender.sendNotification(storyEntity.getSessionId(), WsTypes.STORY_REMOVED, storyId);
        return DefaultResponse.ok();
    }

    /**
     * @param storyDto storyDto
     * @return StoryDto with new id
     * @should throw an exception if sessionId is empty or null
     * @should throw an exception if storyName is empty or null
     * @should throw an exception if storyName contains only spaces
     * @should throw an exception if session does not exist
     * @should throw an exception if user is not admin of the related session
     * @should create a story related to the given sessionId
     * @should send a websocket notification
     */
    public StoryDto createStory(StoryDto storyDto) {
        if (StringUtils.isEmpty(storyDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "session should not be null or empty");
        }

        if (StringUtils.isEmpty(storyDto.getStoryName(), true)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "story name should not be null or empty");
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
     * @param storyId storyId
     * @return empty response
     * @should throw an exception if storyId is empty or null
     * @should throw an exception if story does not exist
     * @should throw an exception if user is not admin of the related session
     * @should set story as ended
     * @should send a websocket notification
     */
    public DefaultResponse endStory(String storyId) {
        if (StringUtils.isEmpty(storyId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }

        final StoryEntity storyEntity = storyRepository.findOne(storyId);
        if (Objects.isNull(storyEntity)) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "story not found with id = " + storyId);
        }

        storyRepository.update(storyId, ImmutableMap.<String, Object>builder()
                .put(StoryEntityDef.ENDED, true)
                .build());

        webSocketSender.sendNotification(storyEntity.getSessionId(), WsTypes.STORY_ENDED, storyId);
        return DefaultResponse.ok();
    }
}
