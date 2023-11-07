package org.davydov.in.controller;

import org.davydov.model.AuthDTO;
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
        AuthDTO authDTO = new AuthDTO("test", "test", 1L);
        doReturn(Optional.of("test token")).when(authService).doAuthorization(authDTO);
        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk());
        verify(authService).doAuthorization(authDTO);
    }

    @Test
    public void thatAuthFailure() throws Exception {
        AuthDTO authDTO = new AuthDTO("test", "test", 1L);
        doThrow(new RuntimeException("Some text error")).when(authService).doAuthorization(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authDTO)))
                .andExpect(status().isBadRequest());
    }
}