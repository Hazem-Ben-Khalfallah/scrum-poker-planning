package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.dto.VoteCreationDto;
import com.blacknebula.scrumpoker.dto.VoteDto;
import com.blacknebula.scrumpoker.service.VoteService;
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

import javax.ws.rs.QueryParam;
import java.util.List;

@CrossOrigin
@RestController
public class VoteRestController {

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
     * @param voteCreationDto vote that will be create
     * @return VoteDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/votes", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<VoteCreationDto> saveVote(@RequestBody VoteCreationDto voteCreationDto) {
        return new ResponseEntity<>(voteService.saveVote(voteCreationDto), HttpStatus.OK);
    }
}