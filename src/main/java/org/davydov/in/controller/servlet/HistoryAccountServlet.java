package org.davydov.in.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.davydov.config.DBConnectionProvider;
import org.davydov.model.ResponseDTO;
import org.davydov.model.ResponseListDTO;
import org.davydov.repository.*;
import org.davydov.service.AuthService;
import org.davydov.service.impl.AuthServiceImpl;
import org.davydov.service.HistoryCreditDebitService;
import org.davydov.service.impl.HistoryCreditDebitServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.davydov.config.PropertyUtils.getProperty;

/**
 * Сервлет для получения истории операций по счету игрока
 */
//@WebServlet("/player/history")
public class HistoryAccountServlet extends HttpServlet {

    private DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
    private PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    private AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    private HistoryCreditDebitRepository historyCreditDebitRepository = new HistoryCreditDebitRepositoryImpl(dbConnectionProvider);

    private AuthService authService = new AuthServiceImpl(playerRepository, authRepository);
    private HistoryCreditDebitService historyCreditDebitService = new HistoryCreditDebitServiceImpl(authService ,historyCreditDebitRepository);
    private ObjectMapper objectMapper = new ObjectMapper();

    public HistoryAccountServlet() throws IOException {
    }

    /**
     * Метод для получения истории операция по счету
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
            List<String> listOperation = historyCreditDebitService.getListOperationAccount(token);
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
