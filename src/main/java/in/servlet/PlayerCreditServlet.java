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

@WebServlet("/player/credit")
public class PlayerCreditServlet extends HttpServlet {

    DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(getProperty("db.url"), getProperty("db.user"), getProperty("db.password"));
    PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    TransactionRepository transactionRepository = new TransactionRepositoryImpl(dbConnectionProvider);
    HistoryCreditDebitRepository historyCreditDebitRepository = new HistoryCreditDebitRepositoryImpl(dbConnectionProvider);
    AuditRepository auditRepository = new AuditRepositoryImpl(dbConnectionProvider);
    AuditService auditService = new AuditServiceImpl(auditRepository);
    AuthService authService = new AuthServiceImpl(playerRepository, authRepository);
    TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
    PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitRepository, auditService);
    ObjectMapper objectMapper = new ObjectMapper();
//    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public PlayerCreditServlet() throws IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("token");
//        Validator validator = factory.getValidator();
        try {
            if (token == null) {
                throw new RuntimeException("token is null");
            }
            AccountOperationDTO accountOperationDTO = objectMapper.readValue(req.getInputStream(), AccountOperationDTO.class);
//            Set<ConstraintViolation<AccountOperationDTO>> validate = validator.validate(accountOperationDTO);
//            validate.stream().findFirst().ifPresent(e -> {throw new RuntimeException(e.getMessage());});
            Long accountPlayer = playerService.creditAccount(token, accountOperationDTO);
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
