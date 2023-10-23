package in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
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

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class RegistrationServletTest {
    @Mock
    PlayerService playerService;

    @InjectMocks
    RegistrationServlet registrationServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RequestDispatcher requestDispatcher;

    private StringWriter stringWriter;
    private PrintWriter printWriter;
    @Mock
    ServletOutputStream outputStream;

    @SneakyThrows
    @BeforeEach
    void setUp() {


        MockitoAnnotations.openMocks(this);
//        stringWriter = new StringWriter();
//        printWriter = new PrintWriter(stringWriter);
        when(response.getOutputStream()).thenReturn(outputStream);
//        when(outputStream.wr)
    }

    @SneakyThrows
    @Test
    void doPost() {
        ServletInputStream inputStream = mock(ServletInputStream.class);
        PlayerDTO playerDTO = new PlayerDTO();

        when(objectMapper.readValue(any(ServletInputStream.class), eq(PlayerDTO.class))).thenReturn(playerDTO);

        registrationServlet.doPost(request, response);
        verify(playerService).create(any());
    }
}