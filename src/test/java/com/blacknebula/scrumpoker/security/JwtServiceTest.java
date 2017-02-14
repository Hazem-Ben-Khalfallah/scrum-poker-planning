package com.blacknebula.scrumpoker.security;

import com.blacknebula.scrumpoker.ApplicationTest;
import com.blacknebula.scrumpoker.builders.SessionEntityBuilder;
import com.blacknebula.scrumpoker.builders.UserEntityBuilder;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hazem
 */
public class JwtServiceTest extends ApplicationTest {
    private final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTY3J1bVBva2VyIiwic3ViIjoiaGF6ZW0iLCJqdGkiOiI5NGQ3MDNmZS1mZGFiLTQxOTEtOTMwNi00NzEwNzQ3Njc5OWMiLCJleHAiOjE0ODcwMjk1OTgsImlhdCI6MTQ4NzAyOTU5Mywic0lkIjoicThlNHNBb0dMQjY3NTYiLCJyb2xlIjoiU0VTU0lPTl9BRE1JTiJ9.XoGLOf7-nvJ6I83PVO2AwbNBFkzo7UMsqpTvOfAOuAY";
    private final String USERNAME = "hazem";
    private final String SESSION_ID = "q8e4sAoGLB6756";

    @Autowired
    private JwtService jwtService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * @verifies disconnect user if token has expired
     * @see JwtService#authenticate(String)
     */
    @Test
    public void authenticate_shouldDisconnectUserIfTokenHasExpired() throws Exception {
        // given
        final SessionEntity sessionEntity = SessionEntityBuilder.builder()
                .withSessionId(SESSION_ID)
                .build();
        sessionRepository.save(sessionEntity);

        final UserEntity connectedUser = UserEntityBuilder.builder()
                .withUsername(USERNAME)
                .withSessionId(SESSION_ID)
                .withConnected(true)
                .build();
        userRepository.save(connectedUser);

        // when
        final ScrumPokerAuthenticationToken authenticate = jwtService.authenticate(EXPIRED_TOKEN);
        // then
        Assertions.assertThat(authenticate).isNull();
        final UserEntity userEntity = userRepository.findUser(SESSION_ID, USERNAME);
        Assertions.assertThat(userEntity.isConnected()).isFalse();
    }

    /**
     * @verifies parse token claims if token is valid
     * @see JwtService#authenticate(String)
     */
    @Test
    public void authenticate_shouldParseTokenClaimsIfTokenIsValid() throws Exception {
        // given
        final String username = "Leo";
        final String sessionId = "sessionId";
        final UserRole role = UserRole.VOTER;
        final String token = jwtService.generate(sessionId, username, role);
        // when
        final ScrumPokerAuthenticationToken authenticate = jwtService.authenticate(token);
        // then
        Assertions.assertThat(authenticate).isNotNull();
        Assertions.assertThat(authenticate.getPrincipal()).isNotNull();
        Assertions.assertThat(authenticate.getPrincipal().getSessionId()).isEqualTo(sessionId);
        Assertions.assertThat(authenticate.getPrincipal().getUsername()).isEqualTo(username);
        Assertions.assertThat(authenticate.getPrincipal().getRole()).isEqualTo(role);
    }

    /**
     * @verifies generate a valid jwt token
     * @see JwtService#generate(String, String, UserRole)
     */
    @Test
    public void generate_shouldGenerateAValidJwtToken() throws Exception {
        // when
        final String token = jwtService.generate("sessionId", "username", UserRole.VOTER);
        // then
        Assertions.assertThat(token).isNotEmpty();
        final String partRegex = "(\\w|\\*|-|\\+)+";
        Assertions.assertThat(token).matches(partRegex + "(\\.)" + partRegex + "(\\.)" + partRegex);
    }

    /**
     * @verifies retrieve expired token content
     * @see JwtService#parse(String)
     */
    @Test
    public void parse_shouldRetrieveExpiredTokenContent() throws Exception {
        // when
        final JwtPayload jwtPayload = jwtService.parse(EXPIRED_TOKEN);
        //then
        Assertions.assertThat(jwtPayload).isNotNull();
        Assertions.assertThat(jwtPayload.getSessionId()).isEqualTo(SESSION_ID);
        Assertions.assertThat(jwtPayload.getUsername()).isEqualTo(USERNAME);
        Assertions.assertThat(jwtPayload.getRole()).isNotEmpty();
    }
}
