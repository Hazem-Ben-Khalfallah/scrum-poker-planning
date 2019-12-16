package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.dto.StoryCreationDto;
import com.blacknebula.scrumpoker.dto.StoryDto;
import com.blacknebula.scrumpoker.service.StoryService;
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

import java.util.List;

@CrossOrigin
@RestController
public class StoryRestController {

    @Autowired
    private StoryService storyService;

    /**
     * @return List of StoryDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<StoryDto>> listStories() {
        return new ResponseEntity<>(storyService.listStories(), HttpStatus.OK);
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