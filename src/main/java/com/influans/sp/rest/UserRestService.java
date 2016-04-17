package com.influans.sp.rest;

import com.influans.sp.dto.UserDto;
import com.influans.sp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestService {
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserDto>> listUsers(@RequestParam("sessionId") String sessionId) {
        return new ResponseEntity<>(userService.listUsers(sessionId), HttpStatus.OK);
    }

}