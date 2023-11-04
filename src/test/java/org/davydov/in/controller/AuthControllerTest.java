package org.davydov.in.controller;

import org.davydov.model.PlayerDTO;
import org.davydov.service.AuthService;
import org.davydov.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        PlayerDTO player = new PlayerDTO("test", "test");
        doReturn(Optional.of("test token")).when(authService).doAuthorization(player);
        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(player)))
                .andExpect(status().isOk());

        verify(authService).doAuthorization(player);
    }

    @Test
    public void thatAuthFailure() throws Exception {
        PlayerDTO player = new PlayerDTO("test", "test");
        doThrow(new RuntimeException("Some text error")).when(authService).doAuthorization(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(player)))
                .andExpect(status().isBadRequest());
    }

}