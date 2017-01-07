package com.influans.sp.service;

import com.google.common.collect.ImmutableList;
import com.influans.sp.ApplicationTest;
import com.influans.sp.builders.*;
import com.influans.sp.dto.VoteDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.entity.VoteEntity;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.repository.VoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author hazem
 */
public class VoteServiceTest extends ApplicationTest {

    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * @verifies throw an exception if storyId is null or empty
     * @see VoteService#listVotes(String)
     */
    @Test
    public void listVotes_shouldThrowAnExceptionIfStoryIdIsNullOrEmpty() throws Exception {
        try {
            voteService.listVotes(null);
            Assert.fail("shouldThrowAnExceptionIfStoryIdIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if story does not exist with given id
     * @see VoteService#listVotes(String)
     */
    @Test
    public void listVotes_shouldThrowAnExceptionIfStoryDoesNotExistWithGivenId() throws Exception {
        try {
            voteService.listVotes("invalid_story_id");
            Assert.fail("shouldThrowAnExceptionIfStoryDoesNotExistWithGivenId");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies return list of votes related to the given story
     * @see VoteService#listVotes(String)
     */
    @Test
    public void listVotes_shouldReturnListOfVotesRelatedToTheGivenStory() throws Exception {
        // given
        final String storyId = "storyId";

        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        final List<VoteEntity> votes = ImmutableList.<VoteEntity>builder()
                .add(VoteEntityBuilder.builder()
                        .withStoryId(storyId)
                        .withVoteId("vote1")
                        .build())
                .add(VoteEntityBuilder.builder()
                        .withStoryId(storyId)
                        .withVoteId("vote2")
                        .build())
                .build();
        voteRepository.save(votes);

        //when
        final List<VoteDto> foundVotes = voteService.listVotes(storyId);
        Assertions.assertThat(foundVotes).hasSize(2);
    }

    /**
     * @verifies throw an exception if voteId is null
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfVoteIdIsNull() throws Exception {
        try {
            voteService.delete(null);
            Assert.fail("shouldThrowAnExceptionIfVoteIdIsNull");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if vote does not exist with given id
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfVoteDoesNotExistWithGivenId() throws Exception {
        try {
            voteService.delete("invalid_vote_id");
            Assert.fail("shouldThrowAnExceptionIfVoteDoesNotExistWithGivenId");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies delete vote with the given id
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldDeleteVoteWithTheGivenId() throws Exception {
        // given
        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId(voteId)
                .build();
        voteRepository.save(voteEntity);

        // when
        voteService.delete(voteId);

        // then
        Assertions.assertThat(voteRepository.exists(voteId)).isFalse();
    }

    /**
     * @verifies throw an exception if storyId is null or empty
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfStoryIdIsNullOrEmpty() throws Exception {
        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withSessionId("sessionId")
                .withUsername("username")
                .withValue("value")
                .build();
        try {
            voteService.saveVote(voteDto);
            Assert.fail("shouldThrowAnExceptionIfStoryIdIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if value is null or empty
     * @see VoteService#saveVote(VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfValueIsNullOrEmpty() throws Exception {
        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withSessionId("sessionId")
                .withStoryId("storyId")
                .withUsername("username")
                .build();
        try {
            voteService.saveVote(voteDto);
            Assert.fail("shouldThrowAnExceptionIfValueIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if withSessionId is null or empty
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfSessionIdIsNullOrEmpty() throws Exception {
        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withStoryId("storyId")
                .withUsername("username")
                .withValue("value")
                .build();
        try {
            voteService.saveVote(voteDto);
            Assert.fail("shouldThrowAnExceptionIfSessionIdIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if withUsername is null or empty
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfUsernameIsNullOrEmpty() throws Exception {
        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withSessionId("sessionId")
                .withStoryId("storyId")
                .withValue("value")
                .build();
        try {
            voteService.saveVote(voteDto);
            Assert.fail("shouldThrowAnExceptionIfUsernameIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if session does not exist with given withSessionId
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfSessionDoesNotExistWithGivenSessionId() throws Exception {
        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withSessionId("sessionId")
                .withStoryId("storyId")
                .withUsername("username")
                .withValue("value")
                .build();
        try {
            voteService.saveVote(voteDto);
            Assert.fail("shouldThrowAnExceptionIfSessionDoesNotExistWithGivenSessionId");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
            Assertions.assertThat(e.getMessage()).startsWith("session not found");
        }
    }

    /**
     * @verifies throw an exception if story does not exist with given Id
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfStoryDoesNotExistWithGivenId() throws Exception {
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId("storyId")
                .withUsername("username")
                .withValue("value")
                .build();
        try {
            voteService.saveVote(voteDto);
            Assert.fail("shouldThrowAnExceptionIfStoryDoesNotExistWithGivenId");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
            Assertions.assertThat(e.getMessage()).startsWith("story not found");
        }
    }

    /**
     * @verifies throw an exception if user does not exist with given withUsername
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfUserDoesNotExistWithGivenUsername() throws Exception {
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId(storyId)
                .withUsername("username")
                .withValue("value")
                .build();
        try {
            voteService.saveVote(voteDto);
            Assert.fail("shouldThrowAnExceptionIfUserDoesNotExistWithGivenUsername");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
            Assertions.assertThat(e.getMessage()).startsWith("user not found");
        }
    }

    /**
     * @verifies Update existing vote if the user has already voted on the given story
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldUpdateExistingVoteIfTheUserHasAlreadyVotedOnTheGivenStory() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId("voteId")
                .withStoryId(storyId)
                .withUsername(username)
                .withValue("1d")
                .build();
        voteRepository.save(voteEntity);

        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId(storyId)
                .withUsername(username)
                .withValue("4h")
                .build();

        // when
        voteService.saveVote(voteDto);

        //then
        voteEntity = voteRepository.findOne(voteEntity.getVoteId());
        Assertions.assertThat(voteEntity).isNotNull();
        Assertions.assertThat(voteEntity.getValue()).isEqualTo(voteDto.getValue());
    }

    /**
     * @verifies create a vote for the given user on the selected story
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldCreateAVoteForTheGivenUserOnTheSelectedStory() throws Exception {
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        final String username = "username";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final VoteDto voteDto = VoteDtoBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId(storyId)
                .withUsername(username)
                .withValue("value")
                .build();

        final VoteDto createdVote = voteService.saveVote(voteDto);
        Assertions.assertThat(createdVote.getVoteId()).isNotNull();
        final VoteEntity voteEntity = voteRepository.findOne(createdVote.getVoteId());
        Assertions.assertThat(voteEntity).isNotNull();
        Assertions.assertThat(voteEntity.getSessionId()).isEqualTo(createdVote.getSessionId());
        Assertions.assertThat(voteEntity.getStoryId()).isEqualTo(createdVote.getStoryId());
        Assertions.assertThat(voteEntity.getUsername()).isEqualTo(createdVote.getUsername());
        Assertions.assertThat(voteEntity.getValue()).isEqualTo(createdVote.getValue());
    }
}
