package org.davydov.in.controller;

import org.davydov.model.PlayerDTO;
import org.davydov.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class RegistrationControllerTest {

    private MockMvc mockMvc;
    private PlayerService playerService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        playerService = mock(PlayerService.class);
        mockMvc = standaloneSetup(new RegistrationController(playerService)).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void testSuccessfulPlayerCreation() throws Exception {
        PlayerDTO player = new PlayerDTO("test", "test");
        doReturn(1L).when(playerService).create(player);
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(player)))
                .andExpect(status().isCreated());
        verify(playerService).create(player);
    }

    @Test
    public void testPlayerCreationFailure() throws Exception {
        PlayerDTO player = new PlayerDTO("test", "test");
        doThrow(new RuntimeException("Some text error")).when(playerService).create(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(player)))
                .andExpect(status().isBadRequest());
    }
}

