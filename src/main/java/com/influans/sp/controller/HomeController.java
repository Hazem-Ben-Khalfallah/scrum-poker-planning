package com.influans.sp.controller;

import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.websocket.WebSocketSender;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private WebSocketSender webSocketSender;

    @MessageMapping("/connect")
    @SendTo("/topic/connect")
    public ResponseEntity<UserEntity> connect(String data) throws Exception {
        final JSONObject obj = new JSONObject(data);
        final String username = (String) obj.get("username");
        final String sessionId = (String) obj.get("sessionId");
        final SessionEntity sessionEntity = sessionRepository.findSessionBySessionId(sessionId);
        UserEntity newUserEntity = null;
        Boolean userExist = false;

        if (sessionEntity != null) {
            final List<UserEntity> users = userRepository.findUsersBySessionId(sessionId);
            for (UserEntity user : users) {
                if (user.getUserId().getEntityId().equals(username)) {
                    userExist = true;
                    break;
                }
            }

        }

        if (!userExist) {
            newUserEntity = new UserEntity(username, sessionId, false);
            userRepository.save(newUserEntity);
        }

        return new ResponseEntity<>(newUserEntity, HttpStatus.OK);
    }

    @MessageMapping("/load_data")
    @SendTo("/topic/load_data")
    public ResponseEntity<SessionEntity> loadData(String data) throws Exception {
        final JSONObject obj = new JSONObject(data);
        final String sessionId = (String) obj.get("sessionId");
        final SessionEntity sessionEntity = sessionRepository.findSessionBySessionId(sessionId);
        return new ResponseEntity<SessionEntity>(sessionEntity, HttpStatus.OK);
    }

}