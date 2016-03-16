package com.influans.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.influans.sp.entity.User;
import com.influans.sp.entity.UserRepository;



@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepo;
	
    @MessageMapping("/new_user")
    @SendTo("/topic/new_user")
    public ResponseEntity<User> new_user(User user) throws Exception {
    	userRepo.save(new User(user.getUsername(), user.getIsAdmin()));
    	System.err.println(user);
    	return new ResponseEntity<User>(user,HttpStatus.OK);
    }
}