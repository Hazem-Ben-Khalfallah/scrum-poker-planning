package com.influans.sp.service;

import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.VoteDto;
import com.influans.sp.entity.VoteEntity;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.VoteRepository;
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
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private WebSocketSender webSocketSender;

    /**
     * @should throw an exception if storyId is null or empty
     * @should throw an exception if story does not exist with given id
     * @should return list of votes related to the given story
     * @param storyId storyId
     * @return list of votes
     */
    public List<VoteDto> listVotes(String storyId) {
        final List<VoteDto> votes = new ArrayList<>();
        voteRepository.findByStoryId(storyId).forEach(voteEntity -> //
                votes.add(new VoteDto(voteEntity.getVoteId(), //
                        voteEntity.getSessionId(), //
                        voteEntity.getStoryId(), //
                        voteEntity.getUsername(), //voteEntity
                        voteEntity.getValue())));
        return votes;
    }

    /**
     * @should throw an exception if voteId is null
     * @should throw an exception if vote does not exist with given id
     * @should delete vote with the given id
     * @param voteId voteId
     * @return empty response
     */
    public DefaultResponse delete(String voteId) {
        final VoteEntity voteEntity = voteRepository.findOne(voteId);
        if (StringUtils.isEmpty(voteId) || voteEntity == null) {
            return DefaultResponse.ko();
        }

        voteRepository.delete(voteId);
        webSocketSender.sendNotification(voteEntity.getSessionId(), WsTypes.VOTE_REMOVED, voteId);
        return DefaultResponse.ok();
    }

    /**
     * @should throw an exception if storyId is null or empty
     * @should throw an exception if sessionId is null or empty
     * @should throw an exception if username is null or empty
     * @should throw an exception if story does not exist with given Id
     * @should throw an exception if user does not exist with given username
     * @should throw an exception if session does not exist with given sessionId
     * @should throw an exception if the user has already voted on the given story
     * @should create a vote for the given user on the selected story
     * @param voteDto voteDto
     * @return voteDto with new id
     */
    public VoteDto saveVote(VoteDto voteDto) {
        if (StringUtils.isEmpty(voteDto.getStoryId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "storyId should not be null or empty");
        }
        if (!storyRepository.exists(voteDto.getStoryId())) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "story not found");
        }

        final VoteEntity voteEntity = new VoteEntity(voteDto);
        voteRepository.save(voteEntity);
        voteDto.setVoteId(voteEntity.getVoteId());

        webSocketSender.sendNotification(voteDto.getSessionId(), WsTypes.VOTE_ADDED, voteDto);
        return voteDto;
    }
}
