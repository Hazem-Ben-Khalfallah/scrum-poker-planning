package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.AppIntegrationTest;
import com.blacknebula.scrumpoker.builders.PrincipalBuilder;
import com.blacknebula.scrumpoker.builders.SessionEntityBuilder;
import com.blacknebula.scrumpoker.builders.UserDtoBuilder;
import com.blacknebula.scrumpoker.builders.UserEntityBuilder;
import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.UserDto;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.ResponseStatus;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.security.SecurityContext;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * @author hazem
 */
public class UserRestControllerTest extends AppIntegrationTest {

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
     * @see UserRestController#listUsers(String)
     */
    @Test
    @SuppressWarnings("unchecked")
    public void listUsers_shouldReturn200Status() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final List<UserEntity> users = ImmutableList.<UserEntity>builder()
                .add(UserEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withUsername("Leo")
                        .build())
                .add(UserEntityBuilder.builder()
                        .withSessionId(sessionId)
                        .withUsername("Leander")
                        .build())
                .build();
        userRepository.save(users);

        // when
        final List<UserDto> response = givenJsonClient()
                .queryParam("sessionId", sessionId)
                .get("/users")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(List.class);

        Assertions.assertThat(response).hasSize(2);
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserRestController#listUsers(String)
     */
    @Test
    public void listUsers_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .queryParam("sessionId", "invalid_session_id")
                .get("/users")
                .then()
                .statusCode(CustomErrorCode.OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/users");
    }

    /**
     * @verifies return 200 status and a not null token
     * @see UserRestController#connect(UserDto, HttpServletResponse)
     */
    @Test
    public void connect_shouldReturn200StatusAndANotNullToken() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId(sessionId)
                .withUsername("Leo")
                .build();

        // when
        final UserDto response = givenJsonClient()
                .body(userDto)
                .post("/users/connect")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .header(SecurityContext.Headers.JWT_TOKEN, IsNull.notNullValue())
                .extract()
                .as(UserDto.class);

        // then
        Assertions.assertThat(response).isNotNull();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserRestController#connect(UserDto, HttpServletResponse)
     */
    @Test
    public void connect_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final UserDto userDto = UserDtoBuilder.builder()
                .withUsername("Leo")
                .build();

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .body(userDto)
                .post("/users/connect")
                .then()
                .statusCode(CustomErrorCode.BAD_ARGS.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/users/connect");
    }

    /**
     * @verifies return 200 status
     * @see UserRestController#disconnect()
     */
    @Test
    public void disconnect_shouldReturn200Status() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String username = "Leo";
        final UserEntity userEntity = UserEntityBuilder.builder()
                .withSessionId(sessionId)
                .withUsername(username)
                .build();
        userRepository.save(userEntity);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        final DefaultResponse response = givenJsonClient()
                .post("/users/disconnect")
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
     * @see UserRestController#disconnect()
     */
    @Test
    public void disconnect_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final Principal principal = PrincipalBuilder.builder()
                .withUsername("Leo")
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .post("/users/disconnect")
                .then()
                .statusCode(CustomErrorCode.UNAUTHORIZED.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/users/disconnect");
    }

    /**
     * @verifies return 200 status
     * @see UserRestController#ban(String)
     */
    @Test
    public void ban_shouldReturn200Status() throws Exception {
        // given
        final String sessionId = "sessionId";
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(sessionId)
                .build();
        sessionRepository.save(sessionEntity);

        final String adminUsername = "Leo";
        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(adminUsername)
                .withSessionId(sessionId)
                .withConnected(true)
                .withAdmin(true)
                .build();
        userRepository.save(connectedUser);

        final String username = "Mike";
        final UserEntity user = UserEntityBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withConnected(true)
                .build();
        userRepository.save(user);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(adminUsername)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        final DefaultResponse response = givenJsonClient()
                .delete("/users/ban/{username}", username)
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
     * @see UserRestController#ban(String)
     */
    @Test
    public void ban_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
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
                .withAdmin(true)
                .build();
        userRepository.save(connectedUser);

        final Principal principal = PrincipalBuilder.builder()
                .withUsername(username)
                .withSessionId(sessionId)
                .withRole(UserRole.SESSION_ADMIN)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .delete("/users/ban/{username}", "invalid_username")
                .then()
                .statusCode(CustomErrorCode.OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/users/ban/invalid_username");
    }
}
