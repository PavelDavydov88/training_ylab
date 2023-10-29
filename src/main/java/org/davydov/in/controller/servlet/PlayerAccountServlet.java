package org.davydov.in.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.davydov.config.DBConnectionProvider;
import org.davydov.model.ResponseDTO;
import org.davydov.repository.*;
import org.davydov.service.*;
import org.davydov.service.impl.AuthServiceImpl;
import org.davydov.service.impl.HistoryCreditDebitServiceImpl;
import org.davydov.service.impl.PlayerServiceImpl;
import org.davydov.service.impl.TransactionServiceImpl;

import java.io.IOException;

import static org.davydov.config.PropertyUtils.getProperty;

/**
 * Сервлет для получения счета игрока
 */
//@WebServlet("/player/account")
public class PlayerAccountServlet extends HttpServlet {

    private DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
    private PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    private AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    private TransactionRepository transactionRepository = new TransactionRepositoryImpl(dbConnectionProvider);
    private HistoryCreditDebitRepository historyCreditDebitRepository = new HistoryCreditDebitRepositoryImpl(dbConnectionProvider);
    private AuthService authService = new AuthServiceImpl(playerRepository, authRepository);
    private HistoryCreditDebitService historyCreditDebitService = new HistoryCreditDebitServiceImpl(authService, historyCreditDebitRepository);
    private TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
    private PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitService);
    private ObjectMapper objectMapper = new ObjectMapper();

    public PlayerAccountServlet() throws IOException {
    }

    /**
     * Метод для получения счета игрока
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
            Long accountPlayer = playerService.getAccount(token);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getOutputStream().write(this.objectMapper.writeValueAsBytes(new ResponseDTO(accountPlayer.toString())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getOutputStream().write(this.objectMapper.writeValueAsBytes(new ResponseDTO(e.getMessage())));
            e.printStackTrace();
        }
    }
}
