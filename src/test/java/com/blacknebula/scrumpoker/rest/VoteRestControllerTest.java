package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.builders.VoteCreationDtoBuilder;
import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.VoteCreationDto;
import com.blacknebula.scrumpoker.dto.VoteDto;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.service.VoteService;
import com.blacknebula.scrumpoker.utils.JsonSerializer;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hazem
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = VoteRestController.class)
@ActiveProfiles("test")
public class VoteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    /**
     * @verifies return 200 status
     * @see VoteRestController#listVote(String)
     */
    @Test
    public void listVote_shouldReturn200Status() throws Exception {
        // given
        final String storyId = "storyId";
        final List<VoteDto> votes = ImmutableList.<VoteDto>builder()
                .add(new VoteDto()
                        .setStoryId(storyId)
                        .setVoteId("vote1"))
                .add(new VoteDto()
                        .setStoryId(storyId)
                        .setVoteId("vote2"))
                .build();
        Mockito.when(voteService.listVotes(anyString()))
                .thenReturn(votes);

        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/votes")
                .param("storyId", storyId))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(votes));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see VoteRestController#listVote(String)
     */
    @Test
    public void listVote_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final String storyId = "storyId";
        Mockito.when(voteService.listVotes(anyString()))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));

        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/votes")
                .param("storyId", storyId))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/votes");
    }

    /**
     * @verifies return 200 status
     * @see VoteRestController#delete(String)
     */
    @Test
    public void delete_shouldReturn200Status() throws Exception {
        // given
        final DefaultResponse defaultResponse = DefaultResponse.ok();
        Mockito.when(voteService.delete(anyString()))
                .thenReturn(defaultResponse);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/votes/{voteId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(defaultResponse));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see VoteRestController#delete(String)
     */
    @Test
    public void delete_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(voteService.delete(anyString()))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/votes/{voteId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/votes/1");
    }

    /**
     * @verifies return 200 status
     * @see VoteRestController#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldReturn200Status() throws Exception {
        // given
        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId("1")
                .withValue("value")
                .build();
        Mockito.when(voteService.saveVote(any(VoteCreationDto.class)))
                .thenReturn(voteCreationDto);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(voteCreationDto)))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(voteCreationDto));

    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see VoteRestController#saveVote(VoteCreationDto)
     */
    @Test
    public void saveVote_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final VoteCreationDto voteCreationDto = VoteCreationDtoBuilder.builder()
                .withStoryId("1")
                .withValue("value")
                .build();
        Mockito.when(voteService.saveVote(any(VoteCreationDto.class)))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(voteCreationDto)))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/votes");
    }
}
