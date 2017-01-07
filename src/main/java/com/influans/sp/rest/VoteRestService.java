package com.influans.sp.rest;

import com.influans.sp.dto.VoteDto;
import com.influans.sp.service.VoteService;
import com.influans.sp.websocket.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController()
public class VoteRestService {

    @Autowired
    private VoteService voteService;

    /**
     * @param storyId story id
     * @return List of VoteDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/votes", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<VoteDto>> listVote(@QueryParam("storyId") String storyId) {
        return new ResponseEntity<>(voteService.listVotes(storyId), HttpStatus.OK);
    }

    /**
     * @param voteId deleted vote id
     * @return empty response
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/votes/{voteId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("voteId") String voteId) {
        return new ResponseEntity<>(voteService.delete(voteId), HttpStatus.OK);
    }

    /**
     * @param voteDto vote that will be create
     * @return VoteDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/votes", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<VoteDto> saveVote(@RequestBody VoteDto voteDto) {
        return new ResponseEntity<>(voteService.saveVote(voteDto), HttpStatus.OK);
    }
}