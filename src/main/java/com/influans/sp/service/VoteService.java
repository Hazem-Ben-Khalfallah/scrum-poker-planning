package com.influans.sp.service;

import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.VoteDto;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.entity.VoteEntity;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.repository.VoteRepository;
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
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebSocketSender webSocketSender;

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
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "no story found with given Id " + storyId);
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
     * @should throw an exception if user is not connected to the related session
     * @should throw an exception if voteId is null
     * @should throw an exception if vote does not exist with given id
     * @should delete vote with the given id
     * @should send a websocket notification
     */
    public DefaultResponse delete(String voteId) {
        if (StringUtils.isEmpty(voteId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "voteId should not be null or empty");
        }

        final VoteEntity voteEntity = voteRepository.findOne(voteId);

        if (Objects.isNull(voteEntity)) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "no vote found with given Id " + voteId);
        }

        voteRepository.delete(voteId);
        webSocketSender.sendNotification(voteEntity.getSessionId(), WsTypes.VOTE_REMOVED, voteId);
        return DefaultResponse.ok();
    }

    /**
     * @param voteDto voteDto
     * @return voteDto with new id
     * @should throw an exception if storyId is null or empty
     * @should throw an exception if sessionId is null or empty
     * @should throw an exception if username is null or empty
     * @should throw an exception if value is null or empty
     * @should throw an exception if story does not exist with given Id
     * @should throw an exception if no user has been connected to the related session with the given username
     * @should throw an exception if user has been disconnected from the related session
     * @should throw an exception if session does not exist with given sessionId
     * @should Update existing vote if the user has already voted on the given story
     * @should create a vote for the given user on the selected story
     * @should send a websocket notification
     */
    public VoteDto saveVote(VoteDto voteDto) {
        if (StringUtils.isEmpty(voteDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "sessionId should not be null or empty");
        }

        if (StringUtils.isEmpty(voteDto.getStoryId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }

        if (StringUtils.isEmpty(voteDto.getUsername())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "username should not be null or empty");
        }

        if (StringUtils.isEmpty(voteDto.getValue())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "value should not be null or empty");
        }

        if (!sessionRepository.exists(voteDto.getSessionId())) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "session not found with id = " + voteDto.getSessionId());
        }

        if (!storyRepository.exists(voteDto.getStoryId())) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "story not found with id = " + voteDto.getStoryId());
        }

        final UserEntity userEntity = userRepository.findUser(voteDto.getSessionId(), voteDto.getUsername());
        if (userEntity == null) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "user not found with username = " + voteDto.getUsername());
        } else if (!userEntity.isConnected()) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED, "user %s has been disconnected  from session %s ", voteDto.getUsername(), voteDto.getSessionId());
        }

        VoteEntity voteEntity = voteRepository.getVoteByUserOnStory(voteDto.getUsername(), voteDto.getStoryId());
        if (Objects.isNull(voteEntity)) {
            voteEntity = new VoteEntity(voteDto);
        } else {
            voteEntity.setValue(voteDto.getValue());
        }

        voteRepository.save(voteEntity);
        voteDto.setVoteId(voteEntity.getVoteId());

        webSocketSender.sendNotification(voteDto.getSessionId(), WsTypes.VOTE_ADDED, voteDto);
        return voteDto;
    }
}
