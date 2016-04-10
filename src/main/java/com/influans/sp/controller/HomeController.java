package com.influans.sp.controller;

import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.User;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.utils.ColorUtils;
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
    private UserRepository userRepo;
    @Autowired
    private SessionRepository sessionRepo;
    @Autowired
    private WebSocketSender webSocketSender;

    @MessageMapping("/connect")
    @SendTo("/topic/connect")
    public ResponseEntity<User> connect(String data) throws Exception {
        JSONObject obj = new JSONObject(data);
        String username = (String) obj.get("username");
        String sessionId = (String) obj.get("sessionId");
        SessionEntity sessionEntity = sessionRepo.findSessionBySessionId(sessionId);
        User new_user = null;
        if (sessionEntity != null) {
            List<User> users = userRepo.findUsersBySessionId(sessionId);
            Boolean userExist = false;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    userExist = true;
                    break;
                }
            }
            if (userExist == false) {
                new_user = new User(username, sessionId, false, ColorUtils.getRandomColor());
            }
        } else {
            new_user = new User(username, sessionId, true, ColorUtils.getRandomColor());
        }
        sessionRepo.save(new SessionEntity(sessionId));
        userRepo.save(new_user);
        return new ResponseEntity<User>(new_user, HttpStatus.OK);
    }

    @MessageMapping("/load_data")
    @SendTo("/topic/load_data")
    public ResponseEntity<SessionEntity> load_data(String data) throws Exception {
        JSONObject obj = new JSONObject(data);
        String sessionId = (String) obj.get("sessionId");
        SessionEntity sessionEntity = sessionRepo.findSessionBySessionId(sessionId);
        return new ResponseEntity<SessionEntity>(sessionEntity, HttpStatus.OK);
    }

}