package in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.DBConnectionProvider;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.AccountOperationDTO;
import model.ResponseDTO;
import repository.*;
import service.*;

import java.io.IOException;

import static config.PropertyUtils.getProperty;

/**
 * Сервлет для выполнения дебита игроки
 */
@WebServlet("/player/debit")
public class PlayerDebitServlet extends HttpServlet {

    private DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(getProperty("db.url"), getProperty("db.user"), getProperty("db.password"));
    private PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    private AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    private TransactionRepository transactionRepository = new TransactionRepositoryImpl(dbConnectionProvider);
    private HistoryCreditDebitRepository historyCreditDebitRepository = new HistoryCreditDebitRepositoryImpl(dbConnectionProvider);
    private AuthService authService = new AuthServiceImpl(playerRepository, authRepository);
    private HistoryCreditDebitService historyCreditDebitService = new HistoryCreditDebitServiceImpl(authService, historyCreditDebitRepository);
    private TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
    private PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitService);
    private ObjectMapper objectMapper = new ObjectMapper();

    public PlayerDebitServlet() throws IOException {
    }

    /**
     * Метод для выполнения дебита игрока
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("token");
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            AccountOperationDTO accountOperationDTO = objectMapper.readValue(req.getInputStream(), AccountOperationDTO.class);
            Long accountPlayer = playerService.debitAccount(token, accountOperationDTO);
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
