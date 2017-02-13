package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.dto.SessionCreationDto;
import com.blacknebula.scrumpoker.enums.CardSetEnum;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.StoryRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.JwtService;
import com.blacknebula.scrumpoker.dto.SessionDto;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.utils.DateUtils;
import com.blacknebula.scrumpoker.utils.HashId;
import com.blacknebula.scrumpoker.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * @author hazem
 */
@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private JwtService jwtService;
    @Value("${application.id}")
    private String applicationId;

    /**
     * @param sessionId session id
     * @return sessionDto
     * @should throw an error if sessionId is null or empty
     * @should throw an error if session does not exist
     * @should return valid session if it exists
     */
    public SessionDto getSession(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "SessionId should not be empty");
        }
        final SessionEntity sessionEntity = sessionRepository.findSessionBySessionId(sessionId);
        if (sessionEntity == null) {
            throw new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Session not found");
        }

        return new SessionDto(sessionEntity);
    }

    /**
     * @param sessionCreationDto created session data
     * @return sessionDto with new sessionId
     * @should throw an error if sessionDto is null
     * @should throw an error if username is null
     * @should throw an error if cardSet is null
     * @should create session and an admin user
     * @should create stories if stories list is not empty
     */
    public SessionCreationDto createSession(SessionCreationDto sessionCreationDto, Consumer<String> connectionConsumer) {
        if (sessionCreationDto == null) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Session should not be null");
        }

        if (StringUtils.isEmpty(sessionCreationDto.getUsername(), true)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Username should not be null");
        }

        if (CardSetEnum.toEnum(sessionCreationDto.getCardSet()) == null) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Username should not be null");
        }

        //save session
        final SessionEntity sessionEntity = sessionCreationDto.toEntity();
        final HashId hashid = new HashId(applicationId);
        sessionEntity.setSessionId(hashid.encrypt(new Random().nextInt(9999), DateUtils.now().getTime()));
        sessionEntity.setSprintName(String.format("Sprint planning %s", DateUtils.format("dd/MM/yyyy")));
        sessionRepository.save(sessionEntity);

        //save stories
        if (!CollectionUtils.isEmpty(sessionCreationDto.getStories())) {
            final List<StoryEntity> storyEntities = sessionCreationDto.toStories(sessionEntity.getSessionId());
            storyRepository.save(storyEntities);
        }
        //save user
        final UserEntity userEntity = new UserEntity(sessionCreationDto.getUsername(), sessionEntity.getSessionId(), true);
        userRepository.save(userEntity);

        // generate JWT token
        final String token = jwtService.generate(sessionEntity.getSessionId(), sessionCreationDto.getUsername(), UserRole.SESSION_ADMIN);
        connectionConsumer.accept(token);

        sessionCreationDto.setSessionId(sessionEntity.getSessionId());

        return sessionCreationDto;
    }
}
