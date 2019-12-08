package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.SessionCreationDto;
import com.blacknebula.scrumpoker.dto.SessionDto;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.service.SessionService;
import com.blacknebula.scrumpoker.utils.JsonSerializer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletResponse;

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
    @WithMockUser(roles = "user")
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
        final SessionDto sessionDto = new SessionDto();
        Mockito.when(sessionService.getSession()).thenReturn(sessionDto);
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

}
