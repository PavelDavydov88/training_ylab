package in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.DBConnectionProvider;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PlayerDTO;
import repository.*;
import service.AuthService;
import service.AuthServiceImpl;

import java.io.IOException;
import java.util.Optional;

import static config.PropertyUtils.getProperty;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(getProperty("db.url"), getProperty("db.user"), getProperty("db.password"));
    PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    AuditRepository auditRepository = new AuditRepositoryImpl(dbConnectionProvider);
    AuthService authService = new AuthServiceImpl(playerRepository, authRepository, auditRepository);
    ObjectMapper objectMapper = new ObjectMapper();

    public AuthServlet() throws IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PlayerDTO playerDTO = objectMapper.readValue(req.getInputStream(), PlayerDTO.class);
        try {
            Optional<String> token = authService.doAuthorization(playerDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("text/html");
            resp.getOutputStream().write(token.get().getBytes());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            resp.getOutputStream().write(e.getMessage().getBytes());
            e.printStackTrace();
        }
    }
}
