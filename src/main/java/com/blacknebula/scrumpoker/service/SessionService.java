package com.blacknebula.scrumpoker.service;

import com.blacknebula.scrumpoker.dto.SessionCreationDto;
import com.blacknebula.scrumpoker.dto.SessionDto;
import com.blacknebula.scrumpoker.dto.ThemeDto;
import com.blacknebula.scrumpoker.entity.SessionEntity;
import com.blacknebula.scrumpoker.entity.StoryEntity;
import com.blacknebula.scrumpoker.entity.UserEntity;
import com.blacknebula.scrumpoker.enums.CardSetEnum;
import com.blacknebula.scrumpoker.enums.UserRole;
import com.blacknebula.scrumpoker.enums.WsTypes;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.repository.SessionRepository;
import com.blacknebula.scrumpoker.repository.StoryRepository;
import com.blacknebula.scrumpoker.repository.UserRepository;
import com.blacknebula.scrumpoker.security.JwtService;
import com.blacknebula.scrumpoker.security.Principal;
import com.blacknebula.scrumpoker.utils.DateUtils;
import com.blacknebula.scrumpoker.utils.HashId;
import com.blacknebula.scrumpoker.utils.StringUtils;
import com.blacknebula.scrumpoker.websocket.WebSocketSender;
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

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final WebSocketSender webSocketSender;

    private final String applicationId;

    public SessionService(SessionRepository sessionRepository,
                          UserRepository userRepository,
                          StoryRepository storyRepository,
                          JwtService jwtService,
                          AuthenticationService authenticationService,
                          WebSocketSender webSocketSender,
                          @Value("${application.id}") String applicationId) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.webSocketSender = webSocketSender;
        this.applicationId = applicationId;
    }

    /**
     * @return sessionDto
     * @should check that the user is authenticated
     * @should return valid session if it exists
     */
    public SessionDto getSession() {
        // check also session validity
        final Principal user = authenticationService.checkAuthenticatedUser();
        final SessionEntity sessionEntity = sessionRepository.findById(user.getSessionId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Session not found"));
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
            throw new CustomException(CustomErrorCode.BAD_ARGS, "CardSet should not be null");
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
            storyRepository.saveAll(storyEntities);
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

    /**
     * @param themeDto new session theme
     * @return themeDto
     * @should check that the user is authenticated as admin
     * @should update session theme
     * @should send a websocket notification
     */
    public ThemeDto setTheme(ThemeDto themeDto) {
        final Principal principal = authenticationService.checkAuthenticatedAdmin();
        final SessionEntity sessionEntity = sessionRepository.findById(principal.getSessionId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.OBJECT_NOT_FOUND, "Session not found"));
        sessionEntity.setCardTheme(themeDto.getCardTheme());
        sessionRepository.save(sessionEntity);
        webSocketSender.sendNotification(sessionEntity.getSessionId(), WsTypes.THEME_CHANGED, themeDto);
        return themeDto;
    }
}
