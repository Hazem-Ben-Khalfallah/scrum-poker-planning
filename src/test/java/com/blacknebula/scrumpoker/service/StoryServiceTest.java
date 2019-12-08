package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.ApplicationTest;
import com.blacknebula.scrumpoker.builders.PrincipalBuilder;
import com.blacknebula.scrumpoker.builders.SessionEntityBuilder;
import com.blacknebula.scrumpoker.builders.StoryCreationDtoBuilder;
import com.blacknebula.scrumpoker.builders.StoryEntityBuilder;
import com.blacknebula.scrumpoker.builders.UserEntityBuilder;
import com.blacknebula.scrumpoker.dto.StoryCreationDto;
import com.blacknebula.scrumpoker.dto.StoryDto;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.enums.WsTypes;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.StoryRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.security.SecurityContext;
import com.blacknebula.scrumpoker.websocket.WebSocketSender;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * @author hazem
 */
public class StoryServiceTest extends ApplicationTest {

    @Autowired
    private StoryService storyService;
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
    }

    /**
     * @verifies check that the user is authenticated
     * @see StoryService#listStories()
     */
    @Test
    public void listStories_shouldCheckThatTheUserIsAuthenticated() throws Exception {
        //given
        securityContext.setPrincipal(null);
        try {
            // when
            storyService.listStories();
            Assert.fail("shouldCheckThatTheUserIsAuthenticated");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * @verifies return stories related to the given session
     * @see StoryService#listStories()
     */
    @Test
    public void listStories_shouldReturnStoriesRelatedToTheGivenSession() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final List<StoryEntity> stories = ImmutableList.<StoryEntity>builder()
                .add(StoryEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withStoryId("story-1")
                        .build())
                .add(StoryEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withStoryId("story-2")
                        .build())
                .build();
        storyRepository.saveAll(stories);

        // when
        final List<StoryDto> foundStories = storyService.listStories();

        // then
        Assertions.assertThat(foundStories).hasSize(2);
    }

    /**
     * @verifies throw an exception if storyId is null or empty
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfStoryIdIsNullOrEmpty() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        try {
            // when
            storyService.delete(null);
            Assert.fail("shouldThrowAnExceptionIfStoryIdIsNullOrEmpty");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if story does not exist
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfStoryDoesNotExist() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);
        try {
            // when
            storyService.delete("invalid_story_id");
            Assert.fail("shouldThrowAnExceptionIfStoryDoesNotExist");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies delete a story
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldDeleteAStory() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId("story-1")
                .withSessionId(sessionId)
                .build();
        storyRepository.save(storyEntity);

        // when
        storyService.delete(storyEntity.getStoryId());

        // then
        Assertions.assertThat(storyRepository.existsById(storyEntity.getStoryId())).isFalse();
    }

    /**
     * @verifies check that the user is authenticated as admin
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldCheckThatTheUserIsAuthenticatedAsAdmin() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);
        try {
            // when
            storyService.delete("story_id");
            Assert.fail("shouldCheckThatTheUserIsAuthenticatedAsAdmin");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
            Assertions.assertThat(e.getMessage()).startsWith("user has not session admin role");
        }
    }

    /**
     * @verifies check that the user is connected to the related session
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldCheckThatTheUserIsConnectedToTheRelatedSession() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withSessionId("other_session")
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);
        try {
            // when
            storyService.delete(storyId);
            Assert.fail("shouldCheckThatTheUserIsConnectedToTheRelatedSession");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringWhitespace("User is not the session admin");
        }
    }

    /**
     * @verifies send a websocket notification
     * @see StoryService#delete(String)
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
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withSessionId(sessionId)
                .withStoryId("story-1")
                .build();
        storyRepository.save(storyEntity);

        // when
        storyService.delete(storyEntity.getStoryId());

        //then
        verify(webSocketSender).sendNotification(storyEntity.getSessionId(), WsTypes.STORY_REMOVED, storyEntity.getStoryId());
    }

    /**
     * @verifies throw an exception if storyName is empty or null
     * @see StoryService#createStory(StoryCreationDto)
     */
    @Test
    public void createStory_shouldThrowAnExceptionIfStoryNameIsEmptyOrNull() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final StoryCreationDto storyCreationDto = StoryCreationDtoBuilder.builder()
                .build();
        try {
            // when
            storyService.createStory(storyCreationDto);
            Assert.fail("shouldThrowAnExceptionIfStoryNameIsEmptyOrNull");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if storyName contains only spaces
     * @see StoryService#createStory(StoryCreationDto)
     */
    @Test
    public void createStory_shouldThrowAnExceptionIfStoryNameContainsOnlySpaces() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final StoryCreationDto storyCreationDto = StoryCreationDtoBuilder.builder()
                .withStoryName("   ")
                .build();
        try {
            // when
            storyService.createStory(storyCreationDto);
            Assert.fail("shouldThrowAnExceptionIfStoryNameIsEmptyOrNull");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies check that the user is authenticated as admin
     * @see StoryService#createStory(StoryCreationDto)
     */
    @Test
    public void createStory_shouldCheckThatTheUserIsAuthenticatedAsAdmin() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);

        final StoryCreationDto storyCreationDto = StoryCreationDtoBuilder.builder()
                .withStoryName("story_name")
                .build();
        try {
            // when
            storyService.createStory(storyCreationDto);
            Assert.fail("shouldCheckThatTheUserIsAuthenticatedAsAdmin");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
            Assertions.assertThat(e.getMessage()).startsWith("user has not session admin role");
        }
    }

    /**
     * @verifies create a story related to the given withSessionId
     * @see StoryService#createStory(StoryCreationDto)
     */
    @Test
    public void createStory_shouldCreateAStoryRelatedToTheGivenSessionId() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final StoryCreationDto storyCreationDto = StoryCreationDtoBuilder.builder()
                .withStoryName("story-name")
                .withOrder(2)
                .build();

        // when
        final StoryCreationDto createdStory = storyService.createStory(storyCreationDto);

        // then
        Assertions.assertThat(createdStory.getStoryId()).isNotNull();
        final StoryEntity storyEntity = storyRepository.findById(createdStory.getStoryId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Story not found"));
        Assertions.assertThat(storyEntity).isNotNull();
        Assertions.assertThat(storyEntity.getStoryName()).isEqualTo(storyCreationDto.getStoryName());
        Assertions.assertThat(storyEntity.getOrder()).isEqualTo(storyCreationDto.getOrder());
    }

    /**
     * @verifies send a websocket notification
     * @see StoryService#createStory(StoryCreationDto)
     */
    @Test
    public void createStory_shouldSendAWebsocketNotification() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final StoryCreationDto storyCreationDto = StoryCreationDtoBuilder.builder()
                .withStoryName("story-name")
                .withOrder(2)
                .build();

        // when
        final StoryCreationDto createdStory = storyService.createStory(storyCreationDto);

        // then
        storyCreationDto.setStoryId(createdStory.getStoryId());
        verify(webSocketSender).sendNotification(sessionId, WsTypes.STORY_ADDED, storyCreationDto);
    }

    /**
     * @verifies throw an exception if storyId is empty or null
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldThrowAnExceptionIfStoryIdIsEmptyOrNull() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        try {
            // when
            storyService.endStory(null);
            Assert.fail("shouldThrowAnExceptionIfStoryIdIsEmptyOrNull");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if story does not exist
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldThrowAnExceptionIfStoryDoesNotExist() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        try {
            // when
            storyService.endStory("invalid_story_id");
            Assert.fail("shouldThrowAnExceptionIfStoryDoesNotExist");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies check that the user is authenticated as admin
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldCheckThatTheUserIsAuthenticatedAsAdmin() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        securityContext.setPrincipal(principal);

        try {
            // when
            storyService.endStory("story_id");
            Assert.fail("shouldCheckThatTheUserIsAuthenticatedAsAdmin");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
            Assertions.assertThat(e.getMessage()).startsWith("user has not session admin role");
        }
    }

    /**
     * @verifies check that the user is connected to the related session
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldCheckThatTheUserIsConnectedToTheRelatedSession() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .withSessionId("other_session_id")
                .build();
        storyRepository.save(storyEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        try {
            // when
            storyService.endStory(storyId);
            Assert.fail("shouldCheckThatTheUserIsConnectedToTheRelatedSession");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.PERMISSION_DENIED);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringWhitespace("User is not the session admin");
        }
    }

    /**
     * @verifies set story as ended
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldSetStoryAsEnded() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .withSessionId(sessionId)
                .build();
        storyRepository.save(storyEntity);

        // when
        storyService.endStory(storyId);

        // then
        final StoryEntity foundStory = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Story not found"));
        Assertions.assertThat(foundStory.isEnded()).isTrue();
    }

    /**
     * @verifies send a websocket notification
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldSendAWebsocketNotification() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        securityContext.setPrincipal(principal);

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .withSessionId(sessionId)
                .build();
        storyRepository.save(storyEntity);

        // when
        storyService.endStory(storyId);

        // then
        verify(webSocketSender).sendNotification(storyEntity.getSessionId(), WsTypes.STORY_ENDED, storyId);
    }
}
