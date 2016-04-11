package com.influans.sp.controller;

import com.influans.sp.dto.UserDto;
import com.influans.sp.entity.UserEntity;
import com.influans.sp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserRestService {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserDto>> listUsers(@RequestParam("sessionId") String sessionId) {
        final List<UserEntity> users = userRepository.findUsersBySessionId(sessionId);
        final List<UserDto> result = users.stream()
                .map(userEntity -> new UserDto(userEntity.getUserId().getEntityId(),
                        userEntity.getUserId().getSessionId(),
                        userEntity.getIsAdmin())).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}