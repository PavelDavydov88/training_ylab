package org.davydov.in.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.in.controller.servlet.AuthServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.davydov.service.AuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

class AuthServletTest {
    @Mock
    AuthService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    ServletOutputStream outputStream;

    @Mock
    Optional<String> optional;
    @InjectMocks
    AuthServlet authServlet;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(response.getOutputStream()).thenReturn(outputStream);
    }

    @SneakyThrows
    @Test
    void thatDonePost() {
        PlayerDTO playerDTO = new PlayerDTO();
        when(objectMapper.readValue(nullable(ServletInputStream.class), eq(PlayerDTO.class))).thenReturn(playerDTO);
        authServlet.doPost(request, response);
        verify(authService).doAuthorization(playerDTO);
        verify(outputStream).write(this.objectMapper.writeValueAsBytes(new ResponseDTO(any(String.class))));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testShouldThrowException() throws IOException, SQLException {
        PlayerDTO playerDTO = new PlayerDTO();
        when(objectMapper.readValue(nullable(ServletInputStream.class), eq(PlayerDTO.class))).thenReturn(playerDTO);
        String testMessage = "this player doesn't exist";
        doThrow(new SQLException(testMessage)).when(authService).doAuthorization(playerDTO);
        authServlet.doPost(request, response);
        verify(outputStream).write(this.objectMapper.writeValueAsBytes(new ResponseDTO(any(String.class))));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}