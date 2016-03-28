package com.influans.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.influans.sp.entity.Session;
import com.influans.sp.repository.SessionRepository;

@RestController		
public class RestAPIController {
	@Autowired
	SessionRepository sessionRepo;
	
    @RequestMapping(value="/load_data",method = RequestMethod.GET)
    public  ResponseEntity<Session> load_data(String sessionId) {  	
    	Session session=sessionRepo.findSessionBySessionId(sessionId);    	
    	return new ResponseEntity<Session>(session,HttpStatus.OK);
    }
}