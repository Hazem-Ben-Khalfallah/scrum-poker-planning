package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.VoteCreationDto;
import com.blacknebula.scrumpoker.dto.VoteDto;
import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.entity.VoteEntity;
import com.blacknebula.scrumpoker.enums.WsTypes;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.StoryRepository;
import com.blacknebula.scrumpoker.repository.VoteRepository;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.utils.StringUtils;
import com.blacknebula.scrumpoker.websocket.WebSocketSender;
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
public class VoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;
    private final StoryRepository storyRepository;
    private final WebSocketSender webSocketSender;
    private final AuthenticationService authenticationService;

    public VoteService(VoteRepository voteRepository,
                       StoryRepository storyRepository,
                       WebSocketSender webSocketSender,
                       AuthenticationService authenticationService) {
        this.voteRepository = voteRepository;
        this.storyRepository = storyRepository;
        this.webSocketSender = webSocketSender;
        this.authenticationService = authenticationService;
    }

    /**
     * @param storyId storyId
     * @return list of votes
     * @should check that the user is authenticated
     * @should throw an exception if user is not connected to the session related to the given story
     * @should throw an exception if storyId is null or empty
     * @should throw an exception if story does not exist with given id
     * @should return list of votes related to the given story
     */
    public List<VoteDto> listVotes(String storyId) {
        final Principal user = authenticationService.checkAuthenticatedUser();

        if (StringUtils.isEmpty(storyId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }

        final StoryEntity storyEntity = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Story not found"));
        if (storyEntity == null) {
            LOGGER.error("no story found with given Id {}", storyId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Story not found");
        } else if (!storyEntity.getSessionId().equals(user.getSessionId())) {
            LOGGER.error("username {} is not permitted to list votes from session {}", user.getUsername(), storyEntity.getSessionId());
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "Permission denied");
        }

        final List<VoteDto> votes = new ArrayList<>();
        voteRepository.findByStoryId(storyId) //
                .forEach(voteEntity -> //
                        votes.add(new VoteDto(voteEntity.getVoteId(), //
                                voteEntity.getSessionId(), //
                                voteEntity.getStoryId(), //
                                voteEntity.getUsername(), //
                                voteEntity.getValue())));
        return votes;
    }

    /**
     * @param voteId voteId
     * @return empty response
     * @should check that the user is authenticated
     * @should throw an exception if voteId is null
     * @should throw an exception if vote does not exist with given id
     * @should throw an exception if user is not the vote owner
     * @should throw an exception if story has been already ended
     * @should delete vote with the given id
     * @should send a websocket notification
     */
    public DefaultResponse delete(String voteId) {
        final Principal user = authenticationService.checkAuthenticatedUser();

        if (StringUtils.isEmpty(voteId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "voteId should not be null or empty");
        }

        final VoteEntity voteEntity = voteRepository.findById(voteId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Vote not found"));

        if (Objects.isNull(voteEntity)) {
            LOGGER.error("no vote found with id = {}", voteId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Vote not found");
        }

        // check story status
        checkStoryStatus(voteEntity.getStoryId());

        if (!voteEntity.getUsername().equals(user.getUsername()) || !voteEntity.getSessionId().equals(user.getSessionId())) {
            LOGGER.error("username {} is not permitted to delete vote {} in session {}", user.getUsername(), voteId, user.getSessionId());
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "Permission denied");
        }

        voteRepository.deleteById(voteId);
        webSocketSender.sendNotification(voteEntity.getSessionId(), WsTypes.VOTE_REMOVED, voteId);
        return DefaultResponse.ok();
    }

    /**
     * @param voteCreationDto voteDto
     * @return voteDto with new id
     * @should check that the user is authenticated
     * @should throw an exception if storyId is null or empty
     * @should throw an exception if value is null or empty
     * @should throw an exception if story does not exist with given Id
     * @should throw an exception if story has been already ended
     * @should Update existing vote if the user has already voted on the given story
     * @should create a vote for the given user on the selected story
     * @should send a websocket notification
     */
    public VoteCreationDto saveVote(VoteCreationDto voteCreationDto) {
        final Principal user = authenticationService.checkAuthenticatedUser();

        if (StringUtils.isEmpty(voteCreationDto.getStoryId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }

        if (StringUtils.isEmpty(voteCreationDto.getValue())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "value should not be null or empty");
        }

        // check story status
        checkStoryStatus(voteCreationDto.getStoryId());

        VoteEntity voteEntity = voteRepository.getVoteByUserOnStory(user.getUsername(), voteCreationDto.getStoryId());
        if (Objects.isNull(voteEntity)) {
            voteEntity = new VoteEntity(voteCreationDto);
            voteEntity.setUsername(user.getUsername());
            voteEntity.setSessionId(user.getSessionId());
        } else {
            voteEntity.setValue(voteCreationDto.getValue());
        }

        voteRepository.save(voteEntity);
        voteCreationDto.setVoteId(voteEntity.getVoteId());

        final VoteDto voteDto = voteCreationDto.toVoteDto();
        voteDto.setSessionId(user.getSessionId());
        voteDto.setUsername(user.getUsername());

        webSocketSender.sendNotification(user.getSessionId(), WsTypes.VOTE_ADDED, voteDto);
        return voteCreationDto;
    }

    private void checkStoryStatus(String storyId) {
        final StoryEntity storyEntity = storyRepository.findById(storyId)
                .orElseThrow(() -> {
                    LOGGER.error("story not found with id = {}", storyId);
                    return new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Story not found");
                });
        if (storyEntity.isEnded()) {
            LOGGER.error("story with id = {} has been already ended", storyId);
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "story has been ended");
        }
    }
}
