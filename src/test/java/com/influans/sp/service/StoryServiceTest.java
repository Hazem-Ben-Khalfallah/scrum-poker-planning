package com.influans.sp.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author hazem
 */
@Ignore
public class StoryServiceTest {
    /**
     * @verifies return stories related to the given session
     * @see StoryService#listStories(String)
     */
    @Test
    public void listStories_shouldReturnStoriesRelatedToTheGivenSession() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if session id is null or empty
     * @see StoryService#listStories(String)
     */
    @Test
    public void listStories_shouldThrowAnExceptionIfSessionIdIsNullOrEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if session id is not valid
     * @see StoryService#listStories(String)
     */
    @Test
    public void listStories_shouldThrowAnExceptionIfSessionIdIsNotValid() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies delete a story
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldDeleteAStory() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if storyId is null or empty
     * @see StoryService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfStoryIdIsNullOrEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if sessionId is empty or null
     * @see StoryService#createStory(com.influans.sp.dto.StoryDto)
     */
    @Test
    public void createStory_shouldThrowAnExceptionIfSessionIdIsEmptyOrNull() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if session does not exist
     * @see StoryService#createStory(com.influans.sp.dto.StoryDto)
     */
    @Test
    public void createStory_shouldThrowAnExceptionIfSessionDoesNotExist() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies create a story related to the given sessionId
     * @see StoryService#createStory(com.influans.sp.dto.StoryDto)
     */
    @Test
    public void createStory_shouldCreateAStoryRelatedToTheGivenSessionId() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if storyId is empty or null
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldThrowAnExceptionIfStoryIdIsEmptyOrNull() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if story does not exist
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldThrowAnExceptionIfStoryDoesNotExist() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies set story as ended
     * @see StoryService#endStory(String)
     */
    @Test
    public void endStory_shouldSetStoryAsEnded() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }
}
