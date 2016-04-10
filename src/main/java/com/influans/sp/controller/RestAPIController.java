package com.influans.sp.controller;

import com.influans.sp.entity.Session;
import com.influans.sp.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestAPIController {
    @Autowired
    SessionRepository sessionRepo;

    @RequestMapping(value = "/session/{sessionId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Session> getSession(@PathVariable("sessionId") String sessionId) {
        final Session session = sessionRepo.findSessionBySessionId(sessionId);
        return new ResponseEntity<Session>(session, HttpStatus.OK);
    }
}