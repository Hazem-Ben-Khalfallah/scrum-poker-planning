package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.AppIntegrationTest;
import com.blacknebula.scrumpoker.builders.*;
import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.VoteCreationDto;
import com.blacknebula.scrumpoker.entity.VoteEntity;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.repository.StoryRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.repository.VoteRepository;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.security.SecurityContext;
import com.google.common.collect.ImmutableList;
import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.VoteDto;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.ResponseStatus;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static com.blacknebula.scrumpoker.exception.CustomErrorCode.OBJECT_NOT_FOUND;
import static com.blacknebula.scrumpoker.exception.CustomErrorCode.UNAUTHORIZED;

/**
 * @author hazem
 */
public class VoteRestControllerTest extends AppIntegrationTest {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private StoryRepository storyRepository;
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
     * @see VoteRestController#listVote(String)
     */
    @Test
    @SuppressWarnings("unchecked")
    public void listVote_shouldReturn200Status() throws Exception {
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

        final String storyId = "storyId";
        final StoryEntity storyEntity = StoryEntityBuilder.builder()
                .withStoryId(storyId)
                .withSessionId(sessionId)
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

        // when
        final List<VoteDto> response = givenJsonClient()
                .queryParam("storyId", storyId)
                .get("/votes")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(List.class);

        // then
        Assertions.assertThat(response).hasSize(2);
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see VoteRestController#listVote(String)
     */
    @Test
    public void listVote_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
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

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .queryParam("storyId", "invalid_story_id")
                .get("/votes")
                .then()
                .statusCode(OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/votes");
    }

    /**
     * @verifies return 200 status
     * @see VoteRestController#delete(String)
     */
    @Test
    public void delete_shouldReturn200Status() throws Exception {
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
                .withSessionId(sessionId)
                .build();
        storyRepository.save(storyEntity);

        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withStoryId(storyId)
                .withVoteId(voteId)
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

        // when
        final DefaultResponse response = givenJsonClient()
                .delete("/votes/{voteId}", voteId)
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
     * @see VoteRestController#delete(String)
     */
    @Test
    public void delete_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
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

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .delete("/votes/{voteId}", "invalid_vote_id")
                .then()
                .statusCode(OBJECT_NOT_FOUND.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/votes/invalid_vote_id");
    }

    /**
     * @verifies return 200 status
     * @see VoteRestController#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldReturn200Status() throws Exception {
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
        final VoteDto response = givenJsonClient()
                .body(voteCreationDto)
                .post("/votes")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(VoteDto.class);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getVoteId()).isNotNull();

    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see VoteRestController#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId("storyId")
                .withValue("value")
                .build();

        final Principal principal = PrincipalBuilder.builder()
                .withUsername("username")
                .withSessionId("sessionId")
                .withRole(UserRole.VOTER)
                .build();
        Mockito.when(securityContext.getAuthenticationContext()).thenReturn(Optional.of(principal));

        // when
        final ErrorResponse errorResponse = givenJsonClient()
                .body(voteCreationDto)
                .post("/votes")
                .then()
                .statusCode(UNAUTHORIZED.getStatusCode())
                .extract()
                .as(ErrorResponse.class);

        // then
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/votes");
    }
}
