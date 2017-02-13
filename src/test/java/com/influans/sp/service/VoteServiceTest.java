package com.influans.sp.service;

import com.google.common.collect.ImmutableList;
import com.influans.sp.ApplicationTest;
import com.influans.sp.builders.*;
import com.influans.sp.dto.VoteCreationDto;
import com.influans.sp.dto.VoteDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.entity.VoteEntity;
import com.influans.sp.enums.UserRole;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.repository.VoteRepository;
import com.influans.sp.security.Principal;
import com.influans.sp.security.SecurityContext;
import com.influans.sp.websocket.WebSocketSender;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

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
    @Autowired
    private WebSocketSender webSocketSender;
    @Autowired
    private SecurityContext securityContext;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(webSocketSender);
        Mockito.reset(securityContext);
    }

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
     * @verifies check that the user is authenticated
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldCheckThatTheUserIsAuthenticated() throws Exception {
        // given
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.empty());
        try {
            //when
            voteService.delete("vote_id");
            Assert.fail("shouldThrowAnExceptionIfUserIsNotConnectedToTheRelatedSession");
        } catch (CustomException e) {
            //then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * @verifies throw an exception if voteId is null
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfVoteIdIsNull() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        try {
            // when
            voteService.delete(null);
            Assert.fail("shouldThrowAnExceptionIfVoteIdIsNull");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if vote does not exist with given id
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfVoteDoesNotExistWithGivenId() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        try {
            // when
            voteService.delete("invalid_vote_id");
            Assert.fail("shouldThrowAnExceptionIfVoteDoesNotExistWithGivenId");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies throw an exception if user is not the vote owner
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfUserIsNotTheVoteOwner() throws Exception {
        // given
        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId(voteId)
                .withStoryId(storyId)
                .withUsername("other_username")
                .withSessionId("other_session_id")
                .build();
        voteRepository.save(voteEntity);

        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        try {
            // when
            voteService.delete(voteId);
            Assert.fail("shouldThrowAnExceptionIfUserIsNotTheVoteOwner");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
        }
    }

    /**
     * @verifies throw an exception if story has been already ended
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfStoryHasBeenAlreadyEnded() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .withEnded(true)
                .build();
        storyRepository.save(storyEntity);

        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId(voteId)
                .withStoryId(storyId)
                .withSessionId(sessionId)
                .withUsername(username)
                .build();
        voteRepository.save(voteEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        try {
            // when
            voteService.delete(voteId);
            Assert.fail("shouldThrowAnExceptionIfStoryHasBeenAlreadyEnded");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
            Assertions.assertThat(e.getMessage()).isEqualTo("story has been ended");
        }
    }

    /**
     * @verifies delete vote with the given id
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldDeleteVoteWithTheGivenId() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId(voteId)
                .withSessionId(sessionId)
                .withStoryId(storyId)
                .withUsername(username)
                .build();
        voteRepository.save(voteEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        voteService.delete(voteId);

        // then
        Assertions.assertThat(voteRepository.exists(voteId)).isFalse();
    }

    /**
     * @verifies send a websocket notification
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldSendAWebsocketNotification() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId(storyId)
                .withVoteId(voteId)
                .withUsername(username)
                .build();
        voteRepository.save(voteEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        voteService.delete(voteId);

        // then
        verify(webSocketSender).sendNotification(voteEntity.getSessionId(), WsTypes.VOTE_REMOVED, voteId);
    }

    /**
     * @verifies check that the user is authenticated
     * @see VoteService#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldCheckThatTheUserIsAuthenticated() throws Exception {
        // given
        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withValue("value")
                .withStoryId("story_id")
                .build();

        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.empty());

        try {
            // when
            voteService.saveVote(voteCreationDto);
            Assert.fail("shouldThrowAnExceptionIfUserIsNotAuthenticated");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * @verifies throw an exception if storyId is null or empty
     * @see VoteService#saveVote(com.influans.sp.dto.VoteCreationDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfStoryIdIsNullOrEmpty() throws Exception {
        // given
        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withValue("value")
                .build();

        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));
        try {
            // when
            voteService.saveVote(voteCreationDto);
            Assert.fail("shouldThrowAnExceptionIfStoryIdIsNullOrEmpty");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if value is null or empty
     * @see VoteService#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfValueIsNullOrEmpty() throws Exception {
        // given
        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId("storyId")
                .build();

        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));
        try {
            // then
            voteService.saveVote(voteCreationDto);
            Assert.fail("shouldThrowAnExceptionIfValueIsNullOrEmpty");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if story does not exist with given Id
     * @see VoteService#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfStoryDoesNotExistWithGivenId() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId("storyId")
                .withValue("value")
                .build();

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        try {
            // when
            voteService.saveVote(voteCreationDto);
            Assert.fail("shouldThrowAnExceptionIfStoryDoesNotExistWithGivenId");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
            Assertions.assertThat(e.getMessage()).startsWith("story not found");
        }
    }

    /**
     * @verifies throw an exception if story has been already ended
     * @see VoteService#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfStoryHasBeenAlreadyEnded() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .withEnded(true)
                .build();
        storyRepository.save(storyEntity);

        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId(storyId)
                .withValue("value")
                .build();

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        try {
            // when
            voteService.saveVote(voteCreationDto);
            Assert.fail("shouldThrowAnExceptionIfStoryHasBeenAlreadyEnded");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
            Assertions.assertThat(e.getMessage()).startsWith("story has been ended");
        }
    }

    /**
     * @verifies throw an exception if no user has been connected to the related session with the given username
     * @see VoteService#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfNoUserHasBeenConnectedToTheRelatedSessionWithTheGivenUsername() throws Exception {
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
                .withSessionId("other_session_id")
                .build();
        userRepository.save(userEntity);

        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId(storyId)
                .withValue("value")
                .build();

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        try {
            voteService.saveVote(voteCreationDto);
            Assert.fail("shouldThrowAnExceptionIfUserDoesNotExistWithGivenUsername");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
            Assertions.assertThat(e.getMessage()).startsWith("Invalid user credentials");
        }
    }

    /**
     * @verifies throw an exception if user has been disconnected from the related session
     * @see VoteService#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfUserHasBeenDisconnectedFromTheRelatedSession() throws Exception {
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

        final String username = "username";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(false)
                .build();
        userRepository.save(userEntity);

        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId(storyId)
                .withValue("value")
                .build();

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        try {
            // when
            voteService.saveVote(voteCreationDto);
            Assert.fail("shouldThrowAnExceptionIfUserHasBeenDisconnectedFromTheRelatedSession");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringWhitespace("User already disconnected from session");
        }
    }

    /**
     * @verifies Update existing vote if the user has already voted on the given story
     * @see VoteService#saveVote(VoteCreationDto)
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

        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId(storyId)
                .withValue("4h")
                .build();

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        voteService.saveVote(voteCreationDto);

        //then
        voteEntity = voteRepository.findOne(voteEntity.getVoteId());
        Assertions.assertThat(voteEntity).isNotNull();
        Assertions.assertThat(voteEntity.getValue()).isEqualTo(voteCreationDto.getValue());
    }

    /**
     * @verifies create a vote for the given user on the selected story
     * @see VoteService#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldCreateAVoteForTheGivenUserOnTheSelectedStory() throws Exception {
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

        final String username = "username";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId(storyId)
                .withValue("value")
                .build();

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        final VoteCreationDto createdVote = voteService.saveVote(voteCreationDto);

        // then
        Assertions.assertThat(createdVote.getVoteId()).isNotNull();
        final VoteEntity voteEntity = voteRepository.findOne(createdVote.getVoteId());
        Assertions.assertThat(voteEntity).isNotNull();
        Assertions.assertThat(voteEntity.getStoryId()).isEqualTo(createdVote.getStoryId());
        Assertions.assertThat(voteEntity.getValue()).isEqualTo(createdVote.getValue());
    }

    /**
     * @verifies send a websocket notification
     * @see VoteService#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldSendAWebsocketNotification() throws Exception {
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

        final String username = "username";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .build();
        userRepository.save(userEntity);

        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId(storyId)
                .withValue("value")
                .build();

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        final VoteCreationDto createdVote = voteService.saveVote(voteCreationDto);

        // then
        final VoteDto sentVote = createdVote.toVoteDto();
        sentVote.setUsername(principal.getUsername());
        sentVote.setSessionId(principal.getSessionId());
        verify(webSocketSender).sendNotification(principal.getSessionId(), WsTypes.VOTE_ADDED, sentVote);
    }
}
