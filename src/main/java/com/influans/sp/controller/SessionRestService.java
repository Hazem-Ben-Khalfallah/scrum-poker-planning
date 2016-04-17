package com.influans.sp.controller;

import com.influans.sp.dto.SessionDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.StoryEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.repository.StoryRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SessionRestService {

    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "/sessions/{sessionId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<SessionDto> getSession(@PathVariable("sessionId") String sessionId) {
        return new ResponseEntity<>(sessionService.getSession(sessionId), HttpStatus.OK);
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SessionDto> createSession(@RequestBody SessionDto sessionDto) {
        return new ResponseEntity<>(sessionService.createSession(sessionDto), HttpStatus.OK);
    }
}