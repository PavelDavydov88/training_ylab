package in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import model.PlayerDTO;
import model.ResponseDTO;
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
    private PlayerService playerService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ServletOutputStream outputStream;

    //    @Mock
//    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//    Validator validator = factory.getValidator();

    @InjectMocks
    private RegistrationServlet registrationServlet;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(response.getOutputStream()).thenReturn(outputStream);
    }

    @SneakyThrows
    @Test
    void thatDonePost() {
        PlayerDTO playerDTO = new PlayerDTO("test", "test");
        when(objectMapper.readValue(nullable(ServletInputStream.class), eq(PlayerDTO.class))).thenReturn(playerDTO);
        registrationServlet.doPost(request, response);
        verify(playerService).create(playerDTO);
        verify(outputStream).write(this.objectMapper.writeValueAsBytes(new ResponseDTO("Player created!")));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testShouldThrowException() throws IOException, SQLException {
        PlayerDTO playerDTO = new PlayerDTO("test", "test");
        when(objectMapper.readValue(nullable(ServletInputStream.class), eq(PlayerDTO.class))).thenReturn(playerDTO);
        String testMessage = "test message";
        doThrow(new SQLException(testMessage)).when(playerService).create(playerDTO);
        registrationServlet.doPost(request, response);
        verify(outputStream).write(this.objectMapper.writeValueAsBytes(new ResponseDTO(anyString())));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}