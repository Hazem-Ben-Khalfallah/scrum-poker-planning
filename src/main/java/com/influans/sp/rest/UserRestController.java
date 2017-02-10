package com.influans.sp.rest;

import com.influans.sp.dto.DefaultResponse;
import com.influans.sp.dto.UserDto;
import com.influans.sp.enums.UserRole;
import com.influans.sp.security.JwtService;
import com.influans.sp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    /**
     * @param sessionId session id
     * @return list of UserDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserDto>> listUsers(@RequestParam("sessionId") String sessionId) {
        return new ResponseEntity<>(userService.listUsers(sessionId), HttpStatus.OK);
    }

    /**
     * @param userDto user that will be connected
     * @return UserDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/users/connect", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UserDto> connect(@RequestBody UserDto userDto, HttpServletResponse httpServletResponse) {
        final ResponseEntity<UserDto> response = new ResponseEntity<>(userService.connectUser(userDto), HttpStatus.OK);
        final UserRole role = userDto.isAdmin() ? UserRole.SESSION_ADMIN : UserRole.VOTER;
        final String token = jwtService.generate(userDto.getSessionId(), userDto.getUsername(), role);
        httpServletResponse.addHeader("jwt-token", token);
        return response;
    }

    /**
     * @param userDto user that will be disconnected
     * @return UserDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/users/disconnect", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultResponse> disconnect(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.disconnectUser(userDto), HttpStatus.OK);
    }

}