package com.blacknebula.scrumpoker.rest;

import com.blacknebula.scrumpoker.dto.DefaultResponse;
import com.blacknebula.scrumpoker.dto.ErrorResponse;
import com.blacknebula.scrumpoker.dto.UserDto;
import com.blacknebula.scrumpoker.exception.CustomErrorCode;
import com.blacknebula.scrumpoker.exception.CustomException;
import com.blacknebula.scrumpoker.service.UserService;
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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hazem
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = UserRestController.class)
@ActiveProfiles("test")
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    /**
     * @verifies return 200 status
     * @see UserRestController#listUsers()
     */
    @Test
    public void listUsers_shouldReturn200Status() throws Exception {
        // given
        final List<UserDto> users = ImmutableList.<UserDto>builder()
                .add(new UserDto()
                        .setSessionId("sessionId")
                        .setUsername("Leo"))
                .add(new UserDto()
                        .setSessionId("sessionId")
                        .setUsername("Leander"))
                .build();
        Mockito.when(userService.listUsers())
                .thenReturn(users);

        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/users"))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(users));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserRestController#listUsers()
     */
    @Test
    public void listUsers_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(userService.listUsers())
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));

        //when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/users"))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/users");
    }

    /**
     * @verifies return 200 status and a not null jwt token
     * @see UserRestController#connect(UserDto, HttpServletResponse)
     */
    @Test
    public void connect_shouldReturn200StatusAndANotNullJwtToken() throws Exception {
        // given
        final UserDto userDto = new UserDto()
                .setSessionId("sessionId")
                .setUsername("Leo");
        Mockito.when(userService.connectUser(any(UserDto.class), any()))
                .thenReturn(userDto);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/connect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(userDto)))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(userDto));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserRestController#connect(UserDto, HttpServletResponse)
     */
    @Test
    public void connect_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        final UserDto userDto = new UserDto()
                .setSessionId("sessionId")
                .setUsername("Leo");
        Mockito.when(userService.connectUser(any(UserDto.class), any()))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/connect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSerializer.serialize(userDto)))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/users/connect");
    }

    /**
     * @verifies return 200 status
     * @see UserRestController#disconnect()
     */
    @Test
    public void disconnect_shouldReturn200Status() throws Exception {
        // given
        final DefaultResponse defaultResponse = DefaultResponse.ok();
        Mockito.when(userService.disconnectUser())
                .thenReturn(defaultResponse);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/disconnect")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(defaultResponse));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserRestController#disconnect()
     */
    @Test
    public void disconnect_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(userService.disconnectUser())
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/disconnect")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/users/disconnect");
    }

    /**
     * @verifies return 200 status
     * @see UserRestController#ban(String)
     */
    @Test
    public void ban_shouldReturn200Status() throws Exception {
        // given
        final DefaultResponse defaultResponse = DefaultResponse.ok();
        Mockito.when(userService.ban(anyString()))
                .thenReturn(defaultResponse);


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/ban/{username}", "mike")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        Assertions.assertThat(jsonContent).isEqualTo(JsonSerializer.serialize(defaultResponse));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserRestController#ban(String)
     */
    @Test
    public void ban_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(userService.ban(anyString()))
                .thenThrow(new CustomException(CustomErrorCode.UNAUTHORIZED, "user not authenticated"));


        // when
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/ban/{username}", "mike")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();

        final String jsonContent = result.getResponse().getContentAsString();
        final ErrorResponse errorResponse = JsonSerializer.toObject(jsonContent, ErrorResponse.class);
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.EXCEPTION)).isNotNull();
        Assertions.assertThat(errorResponse.get(ErrorResponse.Attributes.URI)).isEqualTo("/users/ban/mike");
    }
}
