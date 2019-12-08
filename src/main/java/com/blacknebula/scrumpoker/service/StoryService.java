package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.StoryCreationDto;
import com.blacknebula.scrumpoker.dto.StoryDto;
import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.entity.def.StoryEntityDef;
import com.blacknebula.scrumpoker.enums.WsTypes;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.StoryRepository;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.utils.StringUtils;
import com.blacknebula.scrumpoker.websocket.WebSocketSender;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final StoryRepository storyRepository;
    private final WebSocketSender webSocketSender;
    private final AuthenticationService authenticationService;

    public StoryService(StoryRepository storyRepository,
                        WebSocketSender webSocketSender,
                        AuthenticationService authenticationService) {
        this.storyRepository = storyRepository;
        this.webSocketSender = webSocketSender;
        this.authenticationService = authenticationService;
    }

    /**
     * @return list of stories
     * @should check that the user is authenticated
     * @should return stories related to the given session
     */
    public List<StoryDto> listStories() {
        final Principal user = authenticationService.checkAuthenticatedUser();

        final List<StoryDto> stories = new ArrayList<>();
        storyRepository.findBySessionId(user.getSessionId()).forEach(storyEntity -> //
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

        final StoryEntity storyEntity = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Story not found"));
        if (Objects.isNull(storyEntity)) {
            LOGGER.error("story not found with id = {}", storyId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "story not found");
        }

        if (!storyEntity.getSessionId().equals(principal.getSessionId())) {
            LOGGER.error("User {} is not admin of session {} ", principal.getUsername(), storyEntity.getSessionId());
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "User is not the session admin");
        }

        storyRepository.deleteById(storyId);
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

        final StoryEntity storyEntity = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Story not found"));
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
