package com.influans.sp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.influans.sp.entity.User;

@Controller
public class HomeController {
    @MessageMapping("/new_user")
    @SendTo("/topic/new_user")
    public ResponseEntity<User> new_user(User user) throws Exception {
    	return new ResponseEntity<User>(user,HttpStatus.OK);
    }
}