package com.influans.sp.rest;

import com.google.common.collect.ImmutableList;
import com.influans.sp.AppIntegrationTest;
import com.influans.sp.builders.SessionEntityBuilder;
import com.influans.sp.builders.UserDtoBuilder;
import com.influans.sp.builders.UserEntityBuilder;
import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.ErrorResponse;
import com.influans.sp.dto.UserDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.enums.ResponseStatus;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.List;

import static com.influans.sp.dto.ErrorResponse.Attributes.EXCEPTION;
import static com.influans.sp.dto.ErrorResponse.Attributes.URI;
import static com.influans.sp.exception.CustomErrorCode.BAD_ARGS;
import static com.influans.sp.exception.CustomErrorCode.OBJECT_NOT_FOUND;

/**
 * @author hazem
 */
public class UserRestControllerTest extends AppIntegrationTest {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

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
                .statusCode(OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/users");
    }

    /**
     * @verifies return 200 status
     * @see UserRestController#connect(com.influans.sp.dto.UserDto)
     */
    @Test
    public void connect_shouldReturn200Status() throws Exception {
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
                .extract()
                .as(UserDto.class);

        // then
        Assertions.assertThat(response).isNotNull();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserRestController#connect(com.influans.sp.dto.UserDto)
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
                .statusCode(BAD_ARGS.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/users/connect");
    }

    /**
     * @verifies return 200 status
     * @see UserRestController#disconnect(com.influans.sp.dto.UserDto)
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

        final UserDto userDto = UserDtoBuilder.builder()
                .withSessionId(sessionId)
                .withUsername(username)
                .build();

        // when
        final DefaultResponse response = givenJsonClient()
                .body(userDto)
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
     * @see UserRestController#disconnect(com.influans.sp.dto.UserDto)
     */
    @Test
    public void disconnect_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final UserDto userDto = UserDtoBuilder.builder()
                .withUsername("Leo")
                .build();

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .body(userDto)
                .post("/users/disconnect")
                .then()
                .statusCode(BAD_ARGS.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(URI)).isEqualTo("/users/disconnect");
    }
}
