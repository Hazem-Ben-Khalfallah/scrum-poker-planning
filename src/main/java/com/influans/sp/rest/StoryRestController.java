package com.influans.sp.rest;

import com.influans.sp.dto.StoryCreationDto;
import com.influans.sp.dto.StoryDto;
import com.influans.sp.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController()
public class StoryRestController {

    @Autowired
    private StoryService storyService;

    /**
     * @param sessionId sessionId
     * @return List of StoryDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<StoryDto>> listStories(@QueryParam("sessionId") String sessionId) {
        return new ResponseEntity<>(storyService.listStories(sessionId), HttpStatus.OK);
    }

    /**
     * @param storyId story id
     * @return empty response
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/stories/{storyId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("storyId") String storyId) {
        return new ResponseEntity<>(storyService.delete(storyId), HttpStatus.OK);
    }

    /**
     * @param storyId story id
     * @return empty response
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/stories/{storyId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity endStory(@PathVariable("storyId") String storyId) {
        return new ResponseEntity<>(storyService.endStory(storyId), HttpStatus.OK);
    }

    /**
     * @param storyCreationDto story that will be created
     * @return StoryDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/stories", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<StoryCreationDto> createStory(@RequestBody StoryCreationDto storyCreationDto) {
        return new ResponseEntity<>(storyService.createStory(storyCreationDto), HttpStatus.OK);
    }


}