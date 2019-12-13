package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.builders.SessionCreationDtoBuilder;
import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.SessionCreationDto;
import com.blacknebula.scrumpoker.dto.SessionDto;
import com.blacknebula.scrumpoker.dto.ThemeDto;
import com.blacknebula.scrumpoker.enums.CardSetEnum;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.service.SessionService;
import com.blacknebula.scrumpoker.utils.JsonSerializer;
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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hazem
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = SessionRestController.class)
@ActiveProfiles("test")
public class SessionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    /**
     * @verifies return 200 status
     * @see SessionRestController#getSession()
     */
    @Test
    public void getSession_shouldReturn200Status() throws Exception {
        //given
        final SessionDto sessionDto = new SessionDto();
        Mockito.when(sessionService.getSession()).thenReturn(sessionDto);
        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/sessions"))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(sessionDto));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see SessionRestController#getSession()
     */
    @Test
    public void getSession_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        //given
        Mockito.when(sessionService.getSession())
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));

        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/sessions"))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/sessions");
    }


    /**
     * @verifies return 200 status
     * @see SessionRestController#createSession(SessionCreationDto, HttpServletResponse)
     */
    @Test
    public void createSession_shouldReturn200Status() throws Exception {
        // given
        final SessionCreationDto sessionCreationDto = SessionCreationDtoBuilder.builder()
                .withUsername("username")
                .withCardSet(CardSetEnum.FIBONACCI)
                .build();
        Mockito.when(sessionService.createSession(any(SessionCreationDto.class), any()))
                .thenReturn(sessionCreationDto);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(sessionCreationDto)))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(sessionCreationDto));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see SessionRestController#createSession(SessionCreationDto, HttpServletResponse)
     */
    @Test
    public void createSession_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        //given
        final SessionCreationDto sessionCreationDto = SessionCreationDtoBuilder.builder()
                .withUsername("username")
                .withCardSet(CardSetEnum.FIBONACCI)
                .build();
        Mockito.when(sessionService.createSession(any(SessionCreationDto.class), any()))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));

        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(sessionCreationDto)))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/sessions");
    }

    /**
     * @verifies return 200 status
     * @see SessionRestController#setSessionTheme(com.blacknebula.scrumpoker.dto.ThemeDto, HttpServletResponse)
     */
    @Test
    public void setSessionTheme_shouldReturn200Status() throws Exception {
        // given
        final ThemeDto themeDto = ThemeDto.newBuilder()
                .cardTheme("new theme")
                .build();

        Mockito.when(sessionService.setTheme(any(ThemeDto.class)))
                .thenReturn(themeDto);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put("/sessions/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(themeDto)))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(themeDto));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see SessionRestController#setSessionTheme(com.blacknebula.scrumpoker.dto.ThemeDto, HttpServletResponse)
     */
    @Test
    public void setSessionTheme_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final ThemeDto themeDto = ThemeDto.newBuilder()
                .cardTheme("new theme")
                .build();

        Mockito.when(sessionService.setTheme(any(ThemeDto.class)))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put("/sessions/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(themeDto)))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/sessions/theme");
    }

}
