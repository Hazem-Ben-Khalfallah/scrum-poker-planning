package com.influans.sp.service;

import com.google.common.collect.ImmutableList;
import com.influans.sp.ApplicationTest;
import com.influans.sp.builders.SessionEntityBuilder;
import com.influans.sp.builders.StoryDtoBuilder;
import com.influans.sp.builders.StoryEntityBuilder;
import com.influans.sp.dto.StoryDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    /**
     * @verifies throw an exception if session id is null or empty
     * @see StoryService#listStories(String)
     */
    @Test
    public void listStories_shouldThrowAnExceptionIfSessionIdIsNullOrEmpty() throws Exception {
        try {
            storyService.listStories(null);
            Assert.fail("shouldThrowAnExceptionIfSessionIdIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if session id is not valid
     * @see StoryService#listStories(String)
     */
    @Test
    public void listStories_shouldThrowAnExceptionIfSessionIdIsNotValid() throws Exception {
        try {
            storyService.listStories("invalid_session_id");
            Assert.fail("shouldThrowAnExceptionIfSessionIdIsNotValid");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies return stories related to the given session
     * @see StoryService#listStories(String)
     */
    @Test
    public void listStories_shouldReturnStoriesRelatedToTheGivenSession() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

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
        storyRepository.save(stories);

        // when
        final List<StoryDto> foundStories = storyService.listStories(sessionId);

        // then
        Assertions.assertThat(foundStories).hasSize(2);
    }

    /**
     * @verifies throw an exception if storyId is null or empty
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfStoryIdIsNullOrEmpty() throws Exception {
        try {
            storyService.delete(null);
            Assert.fail("shouldThrowAnExceptionIfStoryIdIsNullOrEmpty");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if story does not exist
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfStoryDoesNotExist() throws Exception {
        try {
            storyService.delete("invalid_story_id");
            Assert.fail("shouldThrowAnExceptionIfStoryDoesNotExist");
        } catch (CustomException e) {
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
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId("story-1")
                .build();
        storyRepository.save(storyEntity);

        // when
        storyService.delete(storyEntity.getStoryId());

        // then
        Assertions.assertThat(storyRepository.exists(storyEntity.getStoryId())).isFalse();
    }

    /**
     * @verifies throw an exception if withSessionId is empty or null
     * @see StoryService#createStory(com.influans.sp.dto.StoryDto)
     */
    @Test
    public void createStory_shouldThrowAnExceptionIfSessionIdIsEmptyOrNull() throws Exception {
        final StoryDto storyDto = StoryDtoBuilder.builder()
                .withStoryName("story-name")
                .build();
        try {
            storyService.createStory(storyDto);
            Assert.fail("shouldThrowAnExceptionIfSessionIdIsNotValid");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if storyName is empty or null
     * @see StoryService#createStory(StoryDto)
     */
    @Test
    public void createStory_shouldThrowAnExceptionIfStoryNameIsEmptyOrNull() throws Exception {
        final StoryDto storyDto = StoryDtoBuilder.builder()
                .withSessionId("session_id")
                .build();
        try {
            storyService.createStory(storyDto);
            Assert.fail("shouldThrowAnExceptionIfStoryNameIsEmptyOrNull");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if storyName contains only spaces
     * @see StoryService#createStory(StoryDto)
     */
    @Test
    public void createStory_shouldThrowAnExceptionIfStoryNameContainsOnlySpaces() throws Exception {
        final StoryDto storyDto = StoryDtoBuilder.builder()
                .withSessionId("session_id")
                .withStoryName("   ")
                .build();
        try {
            storyService.createStory(storyDto);
            Assert.fail("shouldThrowAnExceptionIfStoryNameIsEmptyOrNull");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if session does not exist
     * @see StoryService#createStory(com.influans.sp.dto.StoryDto)
     */
    @Test
    public void createStory_shouldThrowAnExceptionIfSessionDoesNotExist() throws Exception {
        final StoryDto storyDto = StoryDtoBuilder.builder()
                .withSessionId("invalid_session_id")
                .withStoryName("story-name")
                .build();
        try {
            storyService.createStory(storyDto);
            Assert.fail("shouldThrowAnExceptionIfSessionDoesNotExist");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies create a story related to the given withSessionId
     * @see StoryService#createStory(com.influans.sp.dto.StoryDto)
     */
    @Test
    public void createStory_shouldCreateAStoryRelatedToTheGivenSessionId() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final StoryDto storyDto = StoryDtoBuilder.builder()
                .withSessionId(sessionId)
                .withStoryName("story-name")
                .withOrder(2)
                .build();

        // when
        final StoryDto createdStory = storyService.createStory(storyDto);

        // then
        Assertions.assertThat(createdStory.getStoryId()).isNotNull();
        final StoryEntity storyEntity = storyRepository.findOne(createdStory.getStoryId());
        Assertions.assertThat(storyEntity).isNotNull();
        Assertions.assertThat(storyEntity.getSessionId()).isEqualTo(storyDto.getSessionId());
        Assertions.assertThat(storyEntity.getStoryName()).isEqualTo(storyDto.getStoryName());
        Assertions.assertThat(storyEntity.getOrder()).isEqualTo(storyDto.getOrder());
    }

    /**
     * @verifies throw an exception if storyId is empty or null
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldThrowAnExceptionIfStoryIdIsEmptyOrNull() throws Exception {
        try {
            storyService.endStory(null);
            Assert.fail("shouldThrowAnExceptionIfStoryIdIsEmptyOrNull");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.BAD_ARGS);
        }
    }

    /**
     * @verifies throw an exception if story does not exist
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldThrowAnExceptionIfStoryDoesNotExist() throws Exception {
        try {
            storyService.endStory("invalid_story_id");
            Assert.fail("shouldThrowAnExceptionIfStoryDoesNotExist");
        } catch (CustomException e) {
            Assertions.assertThat(e.getCustomErrorCode()).isEqualTo(CustomErrorCode.OBJECT_NOT_FOUND);
        }
    }

    /**
     * @verifies set story as ended
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldSetStoryAsEnded() throws Exception {
        // given
        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .build();
        storyRepository.save(storyEntity);

        // when
        storyService.endStory(storyId);

        // then
        final StoryEntity foundStory = storyRepository.findOne(storyId);
        Assertions.assertThat(foundStory.isEnded()).isTrue();
    }
}
