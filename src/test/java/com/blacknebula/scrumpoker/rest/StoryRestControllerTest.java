package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.builders.StoryCreationDtoBuilder;
import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.StoryCreationDto;
import com.blacknebula.scrumpoker.dto.StoryDto;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.service.StoryService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hazem
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = StoryRestController.class)
@ActiveProfiles("test")
public class StoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoryService storyService;

    /**
     * @verifies return 200 status
     * @see StoryRestController#listStories()
     */
    @Test
    @SuppressWarnings("unchecked")
    public void listStories_shouldReturn200Status() throws Exception {
        // given
        final List<StoryDto> stories = ImmutableList.<StoryDto>builder()
                .add(new StoryDto()
                        .setSessionId("sessionId")
                        .setStoryId("story-1"))
                .add(new StoryDto()
                        .setSessionId("sessionId")
                        .setStoryId("story-2"))
                .build();
        Mockito.when(storyService.listStories())
                .thenReturn(stories);

        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/stories"))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(stories));

    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see StoryRestController#listStories()
     */
    @Test
    public void listStories_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        //given
        Mockito.when(storyService.listStories())
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));

        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/stories"))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/stories");
    }

    /**
     * @verifies return 200 status
     * @see StoryRestController#delete(String)
     */
    @Test
    public void delete_shouldReturn200Status() throws Exception {
        // given
        final DefaultResponse defaultResponse = DefaultResponse.ok();
        Mockito.when(storyService.delete(anyString()))
                .thenReturn(defaultResponse);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/stories/{storyId}", "storyId")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(defaultResponse));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see StoryRestController#delete(String)
     */
    @Test
    public void delete_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(storyService.delete(anyString()))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/stories/{storyId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/stories/1");
    }

    /**
     * @verifies return 200 status
     * @see StoryRestController#endStory(String)
     */
    @Test
    public void endStory_shouldReturn200Status() throws Exception {
        // given
        final DefaultResponse defaultResponse = DefaultResponse.ok();
        Mockito.when(storyService.endStory(anyString()))
                .thenReturn(defaultResponse);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/stories/{storyId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(defaultResponse));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see StoryRestController#endStory(String)
     */
    @Test
    public void endStory_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final DefaultResponse defaultResponse = DefaultResponse.ok();
        Mockito.when(storyService.endStory(anyString()))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/stories/{storyId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/stories/1");
    }

    /**
     * @verifies return 200 status
     * @see StoryRestController#createStory(StoryCreationDto)
     */
    @Test
    public void createStory_shouldReturn200Status() throws Exception {
        // given
        final StoryCreationDto storyCreationDto = StoryCreationDtoBuilder.builder()
                .withStoryName("story-name")
                .withOrder(2)
                .build();
        Mockito.when(storyService.createStory(any(StoryCreationDto.class)))
                .thenReturn(storyCreationDto);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(storyCreationDto)))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(storyCreationDto));

    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see StoryRestController#createStory(StoryCreationDto)
     */
    @Test
    public void createStory_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final StoryCreationDto storyCreationDto = StoryCreationDtoBuilder.builder()
                .withStoryName("story-name")
                .withOrder(2)
                .build();
        Mockito.when(storyService.createStory(any(StoryCreationDto.class)))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(storyCreationDto)))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/stories");
    }
}
