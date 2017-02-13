package com.influans.sp.service;

import com.google.common.collect.ImmutableMap;
import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.StoryCreationDto;
import com.influans.sp.dto.StoryDto;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.def.StoryEntityDef;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.security.Principal;
import com.influans.sp.utils.StringUtils;
import com.influans.sp.websocket.WebSocketSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(StoryService.class);

    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private WebSocketSender webSocketSender;
    @Autowired
    private AuthenticationService authenticationService;

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
            LOGGER.error("session not found with id = {}", sessionId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found");
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
     * @should check that the user is authenticated as admin
     * @should check that the user is connected to the related session
     * @should send a websocket notification
     */
    public DefaultResponse delete(String storyId) {
        final Principal principal = authenticationService.checkAuthenticatedAdmin();

        if (StringUtils.isEmpty(storyId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }

        final StoryEntity storyEntity = storyRepository.findOne(storyId);
        if (Objects.isNull(storyEntity)) {
            LOGGER.error("story not found with id = {}", storyId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "story not found");
        }

        if (!storyEntity.getSessionId().equals(principal.getSessionId())) {
            LOGGER.error("User {} is not admin of session {} ", principal.getUsername(), storyEntity.getSessionId());
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "User is not the session admin");
        }

        storyRepository.delete(storyId);
        webSocketSender.sendNotification(storyEntity.getSessionId(), WsTypes.STORY_REMOVED, storyId);
        return DefaultResponse.ok();
    }

    /**
     * @param storyCreationDto storyDto
     * @return StoryDto with new id
     * @should throw an exception if storyName is empty or null
     * @should throw an exception if storyName contains only spaces
     * @should check that the user is authenticated as admin
     * @should create a story related to the given sessionId
     * @should send a websocket notification
     */
    public StoryCreationDto createStory(StoryCreationDto storyCreationDto) {
        final Principal principal = authenticationService.checkAuthenticatedAdmin();

        if (StringUtils.isEmpty(storyCreationDto.getStoryName(), true)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "story name should not be null or empty");
        }

        final StoryEntity storyEntity = new StoryEntity(principal.getSessionId(), storyCreationDto.getStoryName(),
                storyCreationDto.getOrder());
        storyRepository.save(storyEntity);
        storyCreationDto.setStoryId(storyEntity.getStoryId());
        webSocketSender.sendNotification(storyEntity.getSessionId(), WsTypes.STORY_ADDED, storyCreationDto);
        return storyCreationDto;
    }

    /**
     * @param storyId storyId
     * @return empty response
     * @should throw an exception if storyId is empty or null
     * @should throw an exception if story does not exist
     * @should check that the user is authenticated as admin
     * @should check that the user is connected to the related session
     * @should set story as ended
     * @should send a websocket notification
     */
    public DefaultResponse endStory(String storyId) {
        final Principal principal = authenticationService.checkAuthenticatedAdmin();

        if (StringUtils.isEmpty(storyId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }

        final StoryEntity storyEntity = storyRepository.findOne(storyId);
        if (Objects.isNull(storyEntity)) {
            LOGGER.error("story not found with id = {}", storyId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "story not found ");
        }

        if (!storyEntity.getSessionId().equals(principal.getSessionId())) {
            LOGGER.error("User {} is not admin of session {}", principal.getUsername(), storyEntity.getSessionId());
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "User is not the session admin");
        }

        storyRepository.update(storyId, ImmutableMap.<String, Object>builder()
                .put(StoryEntityDef.ENDED, true)
                .build());

        webSocketSender.sendNotification(storyEntity.getSessionId(), WsTypes.STORY_ENDED, storyId);
        return DefaultResponse.ok();
    }
}
