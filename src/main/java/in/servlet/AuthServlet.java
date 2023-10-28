package in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.DBConnectionProvider;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PlayerDTO;
import model.ResponseDTO;
import repository.*;
import service.AuthService;
import service.AuthServiceImpl;

import java.io.IOException;
import java.util.Optional;

import static config.PropertyUtils.getProperty;

/**
 * Сервлет для авторизации
 */
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(getProperty("db.url"), getProperty("db.user"), getProperty("db.password"));
    private PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    private AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    private AuditRepository auditRepository = new AuditRepositoryImpl(dbConnectionProvider);
    private AuthService authService = new AuthServiceImpl(playerRepository, authRepository);
    private ObjectMapper objectMapper = new ObjectMapper();

    public AuthServlet() throws IOException {
    }

    /**
     * Метод для получения token после авторизации
     *
     * @param req  входные данные запроса
     * @param resp JSON token
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PlayerDTO playerDTO = objectMapper.readValue(req.getInputStream(), PlayerDTO.class);
        try {
            Optional<String> token = authService.doAuthorization(playerDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getOutputStream().write(this.objectMapper.writeValueAsBytes(
                    new ResponseDTO(token.orElse("default token"))));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getOutputStream().write(this.objectMapper.writeValueAsBytes(
                    new ResponseDTO(e.getMessage())));
            e.printStackTrace();
        }
    }
}
