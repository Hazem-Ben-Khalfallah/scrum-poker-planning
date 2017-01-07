package com.influans.sp.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author hazem
 */
@Ignore
public class VoteServiceTest {
    /**
     * @verifies throw an exception if storyId is null or empty
     * @see VoteService#listVotes(String)
     */
    @Test
    public void listVotes_shouldThrowAnExceptionIfStoryIdIsNullOrEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if story does not exist with given id
     * @see VoteService#listVotes(String)
     */
    @Test
    public void listVotes_shouldThrowAnExceptionIfStoryDoesNotExistWithGivenId() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return list of votes related to the given story
     * @see VoteService#listVotes(String)
     */
    @Test
    public void listVotes_shouldReturnListOfVotesRelatedToTheGivenStory() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if voteId is null
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfVoteIdIsNull() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if vote does not exist with given id
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldThrowAnExceptionIfVoteDoesNotExistWithGivenId() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies delete vote with the given id
     * @see VoteService#delete(String)
     */
    @Test
    public void delete_shouldDeleteVoteWithTheGivenId() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if storyId is null or empty
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfStoryIdIsNullOrEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if withSessionId is null or empty
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfSessionIdIsNullOrEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if withUsername is null or empty
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfUsernameIsNullOrEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if story does not exist with given Id
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfStoryDoesNotExistWithGivenId() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if user does not exist with given withUsername
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfUserDoesNotExistWithGivenUsername() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if session does not exist with given withSessionId
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfSessionDoesNotExistWithGivenSessionId() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if the user has already voted on the given story
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldThrowAnExceptionIfTheUserHasAlreadyVotedOnTheGivenStory() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies create a vote for the given user on the selected story
     * @see VoteService#saveVote(com.influans.sp.dto.VoteDto)
     */
    @Test
    public void saveVote_shouldCreateAVoteForTheGivenUserOnTheSelectedStory() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }
}
