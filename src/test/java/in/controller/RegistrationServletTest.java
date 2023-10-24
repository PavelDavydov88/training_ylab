package in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import model.PlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.PlayerService;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class RegistrationServletTest {
    @Mock
    PlayerService playerService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    ServletOutputStream outputStream;

    @InjectMocks
    RegistrationServlet registrationServlet;

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
        registrationServlet.doPost(request, response);
        verify(playerService).create(playerDTO);
        verify(outputStream).write("Player created!".getBytes());
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
//        ServletInputStream inputStream = mock(ServletInputStream.class);
//        when(objectMapper.readValue(any(ServletInputStream.class), eq(PlayerDTO.class))).thenReturn(playerDTO);
//        registrationServlet.doPost(request, response);
//        verify(playerService).create(any());
    }

    @Test
    void testShouldThrowException() throws IOException, SQLException {
        PlayerDTO playerDTO = new PlayerDTO();
        when(objectMapper.readValue(nullable(ServletInputStream.class), eq(PlayerDTO.class))).thenReturn(playerDTO);
        String testMessage = "test message";
        doThrow(new SQLException(testMessage)).when(playerService).create(playerDTO);
        registrationServlet.doPost(request, response);
        verify(outputStream).write(testMessage.getBytes());
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}