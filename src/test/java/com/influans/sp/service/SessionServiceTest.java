package com.influans.sp.service;

import com.influans.sp.ApplicationTest;
import com.influans.sp.builders.SessionDtoBuilder;
import com.influans.sp.builders.SessionEntityBuilder;
import com.influans.sp.dto.SessionDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.enums.CardSetEnum;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author hazem
 */
public class SessionServiceTest extends ApplicationTest {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * @verifies throw an error if withSessionId is null or empty
     * @see SessionService#getSession(String)
     */
    @Test
    public void getSession_shouldThrowAnErrorIfSessionIdIsNullOrEmpty() throws Exception {
        try {
            sessionService.getSession(null);
            Assert.fail("shouldThrowAnErrorIfSessionIdIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an error if session does not exist
     * @see SessionService#getSession(String)
     */
    @Test
    public void getSession_shouldThrowAnErrorIfSessionDoesNotExist() throws Exception {
        try {
            sessionService.getSession("invalid_session_id");
            Assert.fail("shouldThrowAnErrorIfSessionDoesNotExist");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies return valid session if it exists
     * @see SessionService#getSession(String)
     */
    @Test
    public void getSession_shouldReturnValidSessionIfItExists() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        //when
        final SessionDto session = sessionService.getSession(sessionId);

        //then
        Assertions.assertThat(session).isNotNull();
        Assertions.assertThat(session.getSessionId()).isEqualTo(sessionId);
    }

    /**
     * @verifies throw an error if sessionDto is null
     * @see SessionService#createSession(com.influans.sp.dto.SessionDto)
     */
    @Test
    public void createSession_shouldThrowAnErrorIfSessionDtoIsNull() throws Exception {
        try {
            sessionService.createSession(null);
            Assert.fail("shouldThrowAnErrorIfSessionDtoIsNull");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an error if withUsername is null
     * @see SessionService#createSession(com.influans.sp.dto.SessionDto)
     */
    @Test
    public void createSession_shouldThrowAnErrorIfUsernameIsNull() throws Exception {
        final SessionDto sessionDto = SessionDtoBuilder.builder()
                .withCardSet(CardSetEnum.FIBONACCI)
                .build();
        try {
            sessionService.createSession(sessionDto);
            Assert.fail("shouldThrowAnErrorIfUsernameIsNull");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an error if cardSet is null
     * @see SessionService#createSession(com.influans.sp.dto.SessionDto)
     */
    @Test
    public void createSession_shouldThrowAnErrorIfCardSetIsNull() throws Exception {
        final SessionDto sessionDto = SessionDtoBuilder.builder()
                .withUsername("username")
                .build();
        try {
            sessionService.createSession(sessionDto);
            Assert.fail("shouldThrowAnErrorIfCardSetIsNull");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies create session and an admin user
     * @see SessionService#createSession(com.influans.sp.dto.SessionDto)
     */
    @Test
    public void createSession_shouldCreateSessionAndAnAdminUser() throws Exception {
        // given
        final SessionDto sessionDto = SessionDtoBuilder.builder()
                .withUsername("username")
                .withSprintName("sprint")
                .withCardSet(CardSetEnum.FIBONACCI)
                .build();
        //when
        final SessionDto createdSession = sessionService.createSession(sessionDto);

        //then
        Assertions.assertThat(createdSession).isNotNull();
        Assertions.assertThat(createdSession.getSessionId()).isNotNull();

        final SessionEntity sessionEntity = sessionRepository.findOne(createdSession.getSessionId());
        Assertions.assertThat(sessionEntity).isNotNull();
        Assertions.assertThat(sessionEntity.getSessionId()).isNotNull();
        Assertions.assertThat(sessionEntity.getCardSet().name()).isEqualTo(sessionDto.getCardSet());
        Assertions.assertThat(sessionEntity.getSprintName()).isEqualTo(sessionDto.getSprintName());

        final UserEntity userEntity = userRepository.findUser(createdSession.getSessionId(), createdSession.getUsername());
        Assertions.assertThat(userEntity).isNotNull();
        Assertions.assertThat(userEntity.getUserId().getEntityId()).isEqualTo(createdSession.getUsername());
        Assertions.assertThat(userEntity.getIsAdmin()).isTrue();
    }

    /**
     * @verifies create stories if stories list is not empty
     * @see SessionService#createSession(com.influans.sp.dto.SessionDto)
     */
    @Test
    public void createSession_shouldCreateStoriesIfStoriesListIsNotEmpty() throws Exception {
        // given
        final SessionDto sessionDto = SessionDtoBuilder.builder()
                .withUsername("username")
                .withSprintName("sprint")
                .withCardSet(CardSetEnum.FIBONACCI)
                .withStories()
                .addStory("story-1")
                .addStory("story-2")
                .collect()
                .build();
        //when
        final SessionDto createdSession = sessionService.createSession(sessionDto);

        //then
        final List<StoryEntity> storyEntities = storyRepository.findBySessionId(createdSession.getSessionId());
        Assertions.assertThat(storyEntities).hasSize(2);
    }
}
