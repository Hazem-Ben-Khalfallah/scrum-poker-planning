package com.influans.sp.rest;

import com.influans.sp.AppIntegrationTest;
import com.influans.sp.builders.SessionDtoBuilder;
import com.influans.sp.builders.SessionEntityBuilder;
import com.influans.sp.dto.ErrorResponse;
import com.influans.sp.dto.SessionDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.enums.CardSetEnum;
import com.influans.sp.repository.SessionRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;

import static com.influans.sp.dto.ErrorResponse.Attributes.EXCEPTION;
import static com.influans.sp.dto.ErrorResponse.Attributes.URI;
import static com.influans.sp.exception.CustomErrorCode.BAD_ARGS;
import static com.influans.sp.exception.CustomErrorCode.OBJECT_NOT_FOUND;

/**
 * @author hazem
 */
public class SessionRestServiceTest extends AppIntegrationTest {

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * @verifies return 200 status
     * @see SessionRestService#getSession(String)
     */
    @Test
    public void getSession_shouldReturn200Status() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .withCardSet(CardSetEnum.MODIFIED_FIBONACCI)
                .build();
        sessionRepository.save(sessionEntity);

        // when
        final SessionDto sessionDto = givenJsonClient()
                .get("/sessions/{sessionId}", sessionId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(SessionDto.class);

        // then
        Assertions.assertThat(sessionDto).isNotNull();
        Assertions.assertThat(sessionDto.getSessionId()).isEqualTo(sessionEntity.getSessionId());
        Assertions.assertThat(sessionDto.getCardSet()).isEqualTo(sessionEntity.getCardSet().getValue());
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see SessionRestService#getSession(String)
     */
    @Test
    public void getSession_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .get("/sessions/{sessionId}", "invalid_session_id")
                .then()
                .statusCode(OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/sessions/invalid_session_id");
    }

    /**
     * @verifies return 200 status
     * @see SessionRestService#createSession(com.influans.sp.dto.SessionDto)
     */
    @Test
    public void createSession_shouldReturn200Status() throws Exception {
        // given
        final SessionDto sessionDto = SessionDtoBuilder.builder()
                .withUsername("username")
                .withSprintName("sprint")
                .withCardSet(CardSetEnum.FIBONACCI)
                .build();

        // when
        final SessionDto response = givenJsonClient()
                .body(sessionDto)
                .post("/sessions")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(SessionDto.class);

        // then
        Assertions.assertThat(response.getSessionId()).isNotNull();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see SessionRestService#createSession(com.influans.sp.dto.SessionDto)
     */
    @Test
    public void createSession_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        /// given
        final SessionDto sessionDto = SessionDtoBuilder.builder()
                .withSprintName("sprint")
                .withCardSet(CardSetEnum.FIBONACCI)
                .build();

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .body(sessionDto)
                .post("/sessions")
                .then()
                .statusCode(BAD_ARGS.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/sessions");
    }
}
