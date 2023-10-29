package org.davydov.in.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.davydov.config.DBConnectionProvider;
import org.davydov.model.ResponseDTO;
import org.davydov.model.ResponseListDTO;
import org.davydov.repository.*;
import org.davydov.service.AuditService;
import org.davydov.service.impl.AuditServiceImpl;
import org.davydov.service.AuthService;
import org.davydov.service.impl.AuthServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.davydov.config.PropertyUtils.getProperty;

/**
 * Класс сервлета для получения аудита игрока
 */
//@WebServlet("/player/audit")

public class AuditServlet extends HttpServlet {

    private DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
    private PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    private AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    private AuditRepository auditRepository = new AuditRepositoryImpl(dbConnectionProvider);
    private AuthService authService = new AuthServiceImpl(playerRepository, authRepository);
    private AuditService auditService = new AuditServiceImpl(auditRepository, authService);
    private ObjectMapper objectMapper = new ObjectMapper();

    public AuditServlet() throws IOException {
    }

    /**
     * Метод для предоствления аудита игрока
     *
     * @param req  входные данные запроса
     * @param resp JSON ответ на запрос
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            List<String> listOperation = auditService.getListAuditAction(token);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getOutputStream().write(this.objectMapper.writeValueAsBytes(new ResponseListDTO(listOperation)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getOutputStream().write(this.objectMapper.writeValueAsBytes(new ResponseDTO(e.getMessage())));
            e.printStackTrace();
        }
    }
}
