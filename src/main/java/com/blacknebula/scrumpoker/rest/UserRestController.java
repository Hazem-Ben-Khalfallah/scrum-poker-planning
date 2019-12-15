package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.UserDto;
import com.blacknebula.scrumpoker.security.SecurityContext;
import com.blacknebula.scrumpoker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin(exposedHeaders = {SecurityContext.Headers.JWT_TOKEN})
@RestController
public class UserRestController {
    @Autowired
    private UserService userService;

    /**
     * @return list of UserDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserDto>> listUsers() {
        return new ResponseEntity<>(userService.listUsers(), HttpStatus.OK);
    }

    /**
     * @param userDto user that will be connected
     * @return UserDto
     * @should return 200 status and a not null jwt token
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/users/connect", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UserDto> connect(@RequestBody UserDto userDto, HttpServletResponse httpServletResponse) {
        return new ResponseEntity<>(userService.connectUser(userDto,
                (token) -> httpServletResponse.addHeader(SecurityContext.Headers.JWT_TOKEN, token)), HttpStatus.OK);
    }

    /**
     * @return UserDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/users/disconnect", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultResponse> disconnect() {
        return new ResponseEntity<>(userService.disconnectUser(), HttpStatus.OK);
    }

    /**
     * @param username username
     * @return empty response
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/users/ban/{username}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<DefaultResponse> ban(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.ban(username), HttpStatus.OK);
    }


}