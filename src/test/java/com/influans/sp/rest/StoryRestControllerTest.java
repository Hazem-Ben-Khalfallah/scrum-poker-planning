package com.influans.sp.rest;

import com.google.common.collect.ImmutableList;
import com.influans.sp.AppIntegrationTest;
import com.influans.sp.builders.SessionEntityBuilder;
import com.influans.sp.builders.StoryDtoBuilder;
import com.influans.sp.builders.StoryEntityBuilder;
import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.ErrorResponse;
import com.influans.sp.dto.StoryDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.enums.ResponseStatus;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.List;

import static com.influans.sp.dto.ErrorResponse.Attributes.EXCEPTION;
import static com.influans.sp.dto.ErrorResponse.Attributes.URI;
import static com.influans.sp.exception.CustomErrorCode.OBJECT_NOT_FOUND;

/**
 * @author hazem
 */
public class StoryRestControllerTest extends AppIntegrationTest {

    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private SessionRepository sessionRepository;

    /**
     * @verifies return 200 status
     * @see StoryRestController#listStories(String)
     */
    @Test
    @SuppressWarnings("unchecked")
    public void listStories_shouldReturn200Status() throws Exception {
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
        final List<StoryDto> response = givenJsonClient()
                .queryParam("sessionId", sessionId)
                .get("/stories")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(List.class);

        // then
        Assertions.assertThat(response).hasSize(2);
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see StoryRestController#listStories(String)
     */
    @Test
    public void listStories_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .queryParam("sessionId", "invalid_session_id")
                .get("/stories")
                .then()
                .statusCode(OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/stories");
    }

    /**
     * @verifies return 200 status
     * @see StoryRestController#delete(String)
     */
    @Test
    public void delete_shouldReturn200Status() throws Exception {
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

        // when
        final DefaultResponse response = givenJsonClient()
                .delete("/stories/{storyId}", storyId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(DefaultResponse.class);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.OK);
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see StoryRestController#delete(String)
     */
    @Test
    public void delete_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .delete("/stories/{storyId}", "invalid_story_id")
                .then()
                .statusCode(OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/stories/invalid_story_id");
    }

    /**
     * @verifies return 200 status
     * @see StoryRestController#endStory(String)
     */
    @Test
    public void endStory_shouldReturn200Status() throws Exception {
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

        // when
        final DefaultResponse response = givenJsonClient()
                .post("/stories/{storyId}", storyId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(DefaultResponse.class);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.OK);
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see StoryRestController#endStory(String)
     */
    @Test
    public void endStory_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .post("/stories/{storyId}", "invalid_story_id")
                .then()
                .statusCode(OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/stories/invalid_story_id");
    }

    /**
     * @verifies return 200 status
     * @see StoryRestController#createStory(com.influans.sp.dto.StoryDto)
     */
    @Test
    public void createStory_shouldReturn200Status() throws Exception {
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
        final StoryDto response = givenJsonClient()
                .body(storyDto)
                .post("/stories")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(StoryDto.class);

        // then
        Assertions.assertThat(response.getStoryId()).isNotNull();

    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see StoryRestController#createStory(com.influans.sp.dto.StoryDto)
     */
    @Test
    public void createStory_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final StoryDto storyDto = StoryDtoBuilder.builder()
                .withSessionId("invalid_session_id")
                .withStoryName("story-name")
                .withOrder(2)
                .build();

        /// when
        final ErrorResponse errorResponse = givenJsonClient()
                .body(storyDto)
                .post("/stories")
                .then()
                .statusCode(OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/stories");
    }
}
