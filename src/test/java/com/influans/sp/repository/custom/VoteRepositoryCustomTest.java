package com.influans.sp.repository.custom;

import com.influans.sp.ApplicationTest;
import com.influans.sp.builders.VoteEntityBuilder;
import com.influans.sp.entity.VoteEntity;
import com.influans.sp.repository.VoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hazem
 */
public class VoteRepositoryCustomTest extends ApplicationTest {

    @Autowired
    private VoteRepository voteRepository;

    private VoteEntity existingVote;

    @Before
    public void setUp() throws Exception {
        existingVote = createVote();
    }

    /**
     * @verifies return a user vote on a given story
     * @see VoteRepositoryCustom#getVoteByUserOnStory(String, String)
     */
    @Test
    public void getVoteByUserOnStory_shouldReturnAUserVoteOnAGivenStory() throws Exception {
        // when
        final VoteEntity voteByUserOnStory = voteRepository.getVoteByUserOnStory(existingVote.getUsername(), existingVote.getStoryId());
        Assertions.assertThat(voteByUserOnStory).isNotNull();
        Assertions.assertThat(voteByUserOnStory.getStoryId()).isEqualTo(existingVote.getStoryId());
        Assertions.assertThat(voteByUserOnStory.getUsername()).isEqualTo(existingVote.getUsername());
    }

    /**
     * @verifies return null if username is invalid
     * @see VoteRepositoryCustom#getVoteByUserOnStory(String, String)
     */
    @Test
    public void getVoteByUserOnStory_shouldReturnNullIfUsernameIsInvalid() throws Exception {
        // when
        final VoteEntity voteByUserOnStory = voteRepository.getVoteByUserOnStory(existingVote.getUsername(), "invalid_userName");
        Assertions.assertThat(voteByUserOnStory).isNull();
    }

    /**
     * @verifies return null uf storyId is invalid
     * @see VoteRepositoryCustom#getVoteByUserOnStory(String, String)
     */
    @Test
    public void getVoteByUserOnStory_shouldReturnNullIfStoryIdIsInvalid() throws Exception {
        // when
        final VoteEntity voteByUserOnStory = voteRepository.getVoteByUserOnStory("invalid_username", existingVote.getStoryId());
        Assertions.assertThat(voteByUserOnStory).isNull();
    }

    private VoteEntity createVote() {
        final String username = "Leo";
        final String storyId = "storyId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withStoryId(storyId)
                .withStoryId(username)
                .build();
        voteRepository.save(voteEntity);
        return voteEntity;
    }
}
