package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.AppIntegrationTest;
import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.SessionCreationDto;
import com.blacknebula.scrumpoker.enums.CardSetEnum;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.builders.SessionCreationDtoBuilder;
import com.blacknebula.scrumpoker.builders.SessionEntityBuilder;
import com.blacknebula.scrumpoker.dto.SessionDto;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

/**
 * @author hazem
 */
public class SessionRestControllerTest extends AppIntegrationTest {

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * @verifies return 200 status
     * @see SessionRestController#getSession(String)
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
     * @see SessionRestController#getSession(String)
     */
    @Test
    public void getSession_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .get("/sessions/{sessionId}", "invalid_session_id")
                .then()
                .statusCode(CustomErrorCode.OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/sessions/invalid_session_id");
    }

    /**
     * @verifies return 200 status
     * @see SessionRestController#createSession(SessionCreationDto, HttpServletResponse)
     */
    @Test
    public void createSession_shouldReturn200Status() throws Exception {
        // given
        final SessionCreationDto sessionCreationDto = SessionCreationDtoBuilder.builder()
                .withUsername("username")
                .withCardSet(CardSetEnum.FIBONACCI)
                .build();

        // when
        final SessionDto response = givenJsonClient()
                .body(sessionCreationDto)
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
     * @see SessionRestController#createSession(SessionCreationDto, HttpServletResponse)
     */
    @Test
    public void createSession_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        /// given
        final SessionCreationDto sessionCreationDto = SessionCreationDtoBuilder.builder()
                .withCardSet(CardSetEnum.FIBONACCI)
                .build();

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .body(sessionCreationDto)
                .post("/sessions")
                .then()
                .statusCode(CustomErrorCode.BAD_ARGS.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/sessions");
    }
}
