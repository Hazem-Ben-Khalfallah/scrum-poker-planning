package com.influans.sp.controller;

import com.influans.sp.dto.StoryDto;
import com.influans.sp.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController()
public class StoryRestService {

    @Autowired
    private StoryService storyService;

    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<StoryDto>> listStories(@QueryParam("sessionId") String sessionId) {
        return new ResponseEntity<>(storyService.listStories(sessionId), HttpStatus.OK);
    }

    @RequestMapping(value = "/stories/{storyId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("storyId") String storyId) {
        return new ResponseEntity<>(storyService.delete(storyId), HttpStatus.OK);
    }

    @RequestMapping(value = "/stories", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<StoryDto> createStory(@RequestBody StoryDto storyDto) {
        return new ResponseEntity<>(storyService.createStory(storyDto), HttpStatus.OK);
    }
}