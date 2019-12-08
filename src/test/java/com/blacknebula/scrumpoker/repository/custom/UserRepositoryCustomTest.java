package com.blacknebula.scrumpoker.repository.custom;

import com.blacknebula.scrumpoker.ApplicationTest;
import com.blacknebula.scrumpoker.builders.UserEntityBuilder;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author hazem
 */
public class UserRepositoryCustomTest extends ApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * @verifies return users list related to a given session
     * @see UserRepositoryCustom#findUsersBySessionId(String)
     */
    @Test
    public void findUsersBySessionId_shouldReturnUsersListRelatedToAGivenSession() throws Exception {
        // given
        final String sessionId = "sessionId";
        final List<UserEntity> users = ImmutableList.<UserEntity>builder()
                .add(createUser(sessionId, "Leo"))
                .add(createUser(sessionId, "Leander"))
                .build();
        userRepository.saveAll(users);

        // when
        final List<UserEntity> usersBySessionId = userRepository.findUsersBySessionId(sessionId);
        // then
        Assertions.assertThat(usersBySessionId).hasSize(2);
    }

    /**
     * @verifies return empty list if sessionId is invalid
     * @see UserRepositoryCustom#findUsersBySessionId(String)
     */
    @Test
    public void findUsersBySessionId_shouldReturnEmptyListIfSessionIdIsInvalid() throws Exception {
        // when
        final List<UserEntity> usersBySessionId = userRepository.findUsersBySessionId("invalid_session_id");
        // then
        Assertions.assertThat(usersBySessionId).hasSize(0);
    }

    /**
     * @verifies return user connected to a given session with a given username
     * @see UserRepositoryCustom#findUser(String, String)
     */
    @Test
    public void findUser_shouldReturnUserConnectedToAGivenSessionWithAGivenUsername() throws Exception {
        // given
        final String sessionId = "sessionId";
        final String username = "Leo";
        final UserEntity userEntity = createUser(sessionId, username);
        userRepository.save(userEntity);

        // when
        final UserEntity result = userRepository.findUser(sessionId, username);
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUserId().getEntityId()).isEqualTo(username);
        Assertions.assertThat(result.getUserId().getSessionId()).isEqualTo(sessionId);
    }

    /**
     * @verifies return null if sessionId is invalid
     * @see UserRepositoryCustom#findUser(String, String)
     */
    @Test
    public void findUser_shouldReturnNullIfSessionIdIsInvalid() throws Exception {
        // given
        final String sessionId = "sessionId";
        final String username = "Leo";
        final UserEntity userEntity = createUser(sessionId, username);
        userRepository.save(userEntity);

        // when
        final UserEntity result = userRepository.findUser("invalid_session_id", username);
        // then
        Assertions.assertThat(result).isNull();
    }

    /**
     * @verifies return null if username is invalid
     * @see UserRepositoryCustom#findUser(String, String)
     */
    @Test
    public void findUser_shouldReturnNullIfUsernameIsInvalid() throws Exception {
        /// given
        final String sessionId = "sessionId";
        final String username = "Leo";
        final UserEntity userEntity = createUser(sessionId, username);
        userRepository.save(userEntity);

        // when
        final UserEntity result = userRepository.findUser(sessionId, "invalid_username");
        // then
        Assertions.assertThat(result).isNull();
    }

    private UserEntity createUser(String sessionId, String username) {
        return UserEntityBuilder.builder()
                .withSessionId(sessionId)
                .withUsername(username)
                .build();
    }
}
