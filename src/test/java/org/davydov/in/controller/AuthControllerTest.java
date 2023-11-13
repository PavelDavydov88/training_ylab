package org.davydov.in.controller;

import org.davydov.model.PlayerDTO;
import org.davydov.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class AuthControllerTest {
    private MockMvc mockMvc;
    private AuthService authService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        mockMvc = standaloneSetup(new AuthController(authService)).build();
        mapper = new ObjectMapper();
    }

    @Test
    void thatSuccessfulAuth() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO("test", "test");
        doReturn(Optional.of("test token")).when(authService).doAuthorization(1L, playerDTO);
        mockMvc.perform(post("/auth/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(playerDTO)))
                .andExpect(status().isOk());
        verify(authService).doAuthorization(1L, playerDTO);
    }

    @Test
    public void thatAuthFailure() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO("test", "test");
        doThrow(new RuntimeException("Some text error")).when(authService).doAuthorization(1L, playerDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(playerDTO)))
                .andExpect(status().isBadRequest());
    }
}