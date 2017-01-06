package com.influans.sp.service;

import com.influans.sp.dto.SessionDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.enums.CardSetEnum;
import com.influans.sp.exception.CustomErrorCode;
import com.influans.sp.exception.CustomException;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * @should throw an error if session does not exist
     * @should return valid session if it exists
     * @param sessionId session id
     * @return sessionDto
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
     * @should throw an error if sessionDto is null
     * @should throw an error if username is null
     * @should throw an error if cardSet is null
     * @should create session and an admin user
     * @should create stories if stories list is not empty
     * @param sessionDto created session data
     * @return sessionDto with new sessionId
     */
    public SessionDto createSession(SessionDto sessionDto) {
        if (sessionDto == null) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Session should not be null");
        }

        if (StringUtils.isEmpty(sessionDto.getUsername(), true)) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Username should not be null");
        }

        if (CardSetEnum.toEnum(sessionDto.getCardSet()) == null) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "Username should not be null");
        }

        //save session
        final SessionEntity sessionEntity = sessionDto.toEntity();
        sessionRepository.save(sessionEntity);
        //save stories
        if (!sessionDto.getStories().isEmpty()) {
            final List<StoryEntity> storyEntities = sessionDto.toStories(sessionEntity.getSessionId());
            storyRepository.save(storyEntities);
        }
        //save user
        final UserEntity userEntity = new UserEntity(sessionDto.getUsername(), sessionDto.getSessionId(), true);
        userRepository.save(userEntity);

        sessionDto.setSessionId(sessionEntity.getSessionId());
        return sessionDto;
    }
}
