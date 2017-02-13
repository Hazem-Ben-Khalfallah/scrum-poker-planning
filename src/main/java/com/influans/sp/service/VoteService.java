package com.influans.sp.service;

import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.VoteCreationDto;
import com.influans.sp.dto.VoteDto;
import com.influans.sp.entity.VoteEntity;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.VoteRepository;
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
public class VoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private WebSocketSender webSocketSender;
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * @param storyId storyId
     * @return list of votes
     * @should throw an exception if storyId is null or empty
     * @should throw an exception if story does not exist with given id
     * @should return list of votes related to the given story
     */
    public List<VoteDto> listVotes(String storyId) {
        if (StringUtils.isEmpty(storyId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }

        if (!storyRepository.exists(storyId)) {
            LOGGER.error("no story found with given Id {}", storyId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Story not found");
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
     * @should delete vote with the given id
     * @should send a websocket notification
     */
    public DefaultResponse delete(String voteId) {
        final Principal user = authenticationService.checkAuthenticatedUser();

        if (StringUtils.isEmpty(voteId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "voteId should not be null or empty");
        }

        final VoteEntity voteEntity = voteRepository.findOne(voteId);

        if (Objects.isNull(voteEntity)) {
            LOGGER.error("no vote found with id = {}", voteId);
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Vote not found");
        }

        if (!voteEntity.getUsername().equals(user.getUsername()) || !voteEntity.getSessionId().equals(user.getSessionId())) {
            LOGGER.error("username {} is not permitted to delete vote {} in session {}", user.getUsername(), voteId, user.getSessionId());
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "Permission denied");
        }

        voteRepository.delete(voteId);
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

        if (!storyRepository.exists(voteCreationDto.getStoryId())) {
            LOGGER.error("story not found with id = {}" , voteCreationDto.getStoryId());
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "story not found");
        }

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
}
