package org.davydov.in.controller;

import com.jayway.jsonpath.JsonPath;
import org.davydov.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class AccountControllerTest {

    private MockMvc mockMvc;
    private PlayerService playerService;
    private ObjectMapper mapper;


    @BeforeEach
    void setUp() {
        playerService = mock(PlayerService.class);
        mockMvc = standaloneSetup(new AccountController(playerService)).build();
        mapper = new ObjectMapper();
    }

    @Test
    void thatGetAccount() throws Exception {
        String token = "test";
        doReturn(100L).when(playerService).getAccount(anyString());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .header("token", token))
                .andExpect(status().isOk())
                .andReturn();

        String responseAccount = JsonPath.read(result.getResponse().getContentAsString(), "$.response");
        assertEquals("100", responseAccount);
        verify(playerService).getAccount(token);
    }

    @Test
    void thatGetAccountFailure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .header("not a token", ""))
                .andExpect(status().isBadRequest());
    }
}