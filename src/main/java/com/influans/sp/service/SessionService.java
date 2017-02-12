package com.influans.sp.service;

import com.influans.sp.dto.SessionCreationDto;
import com.influans.sp.dto.SessionDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.enums.CardSetEnum;
import com.influans.sp.enums.UserRole;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.security.JwtService;
import com.influans.sp.utils.DateUtils;
import com.influans.sp.utils.HashId;
import com.influans.sp.utils.StringUtils;
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
    @Value("application.id")
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
