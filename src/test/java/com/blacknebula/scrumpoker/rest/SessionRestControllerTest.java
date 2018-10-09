package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.AppIntegrationTest;
import com.blacknebula.scrumpoker.builders.PrincipalBuilder;
import com.blacknebula.scrumpoker.builders.UserEntityBuilder;
import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.SessionCreationDto;
import com.blacknebula.scrumpoker.dto.ThemeDto;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.CardSetEnum;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.builders.SessionCreationDtoBuilder;
import com.blacknebula.scrumpoker.builders.SessionEntityBuilder;
import com.blacknebula.scrumpoker.dto.SessionDto;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.security.SecurityContext;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * @author hazem
 */
public class SessionRestControllerTest extends AppIntegrationTest {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityContext securityContext;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(securityContext);
    }

    /**
     * @verifies return 200 status
     * @see SessionRestController#getSession()
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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        final SessionDto sessionDto = givenJsonClient()
                .get("/sessions")
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
     * @see SessionRestController#getSession()
     */
    @Test
    public void getSession_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        //given
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.empty());

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .get("/sessions")
                .then()
                .statusCode(CustomErrorCode.UNAUTHORIZED.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/sessions");
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
        // given
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

    /**
     * @verifies return 200 status
     * @see SessionRestController#setSessionTheme(com.blacknebula.scrumpoker.dto.ThemeDto, HttpServletResponse)
     */
    @Test
    public void setSessionTheme_shouldReturn200Status() throws Exception {
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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        final ThemeDto themeDto = ThemeDto.newBuilder()
                .cardTheme("new theme")
                .build();

        // when
        final ThemeDto response = givenJsonClient()
                .body(themeDto)
                .put("/sessions/theme")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(ThemeDto.class);

        // then
        Assertions.assertThat(response).isNotNull();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see SessionRestController#setSessionTheme(com.blacknebula.scrumpoker.dto.ThemeDto, HttpServletResponse)
     */
    @Test
    public void setSessionTheme_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
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
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        final ThemeDto themeDto = ThemeDto.newBuilder()
                .cardTheme("new theme")
                .build();

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .body(themeDto)
                .put("/sessions/theme")
                .then()
                .statusCode(CustomErrorCode.PERMISSION_DENIED.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/sessions/theme");
    }
}
