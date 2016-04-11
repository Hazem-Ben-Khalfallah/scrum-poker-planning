package com.influans.sp.controller;

import com.influans.sp.dto.SessionDto;
import com.influans.sp.entity.SessionEntity;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionRestService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/session/{sessionId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<SessionEntity> getSession(@PathVariable("sessionId") String sessionId) {
        final SessionEntity sessionEntity = sessionRepository.findSessionBySessionId(sessionId);
        return new ResponseEntity<>(sessionEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SessionEntity> createSession(@RequestBody SessionDto sessionDto) {
        final SessionEntity sessionEntity = sessionDto.toEntity();
        sessionRepository.save(sessionEntity);

        final UserEntity userEntity = new UserEntity(sessionDto.getUsername(), sessionDto.getSessionId(), true);
        userRepository.save(userEntity);
        return new ResponseEntity<>(sessionEntity, HttpStatus.OK);
    }
}