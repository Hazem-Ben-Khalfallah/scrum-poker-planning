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
        UserEntity user = null;

        if (sessionEntity != null) {
            user = userRepository.findUser(sessionId, username);
        }

        if (user == null) {
            user = new UserEntity(username, sessionId, false);
            userRepository.save(user);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}