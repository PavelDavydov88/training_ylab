package org.davydov.in.controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.davydov.config.DBConnectionProvider;
import org.davydov.model.PlayerDTO;
import org.davydov.model.ResponseDTO;
import org.davydov.repository.*;
import org.davydov.service.*;
import org.davydov.service.impl.AuthServiceImpl;
import org.davydov.service.impl.HistoryCreditDebitServiceImpl;
import org.davydov.service.impl.PlayerServiceImpl;
import org.davydov.service.impl.TransactionServiceImpl;
import org.davydov.utils.ValidationUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import static org.davydov.config.PropertyUtils.getProperty;

/**
 * Сервлет для регистрации игрока
 */
@Slf4j
//@WebServlet("/registration")
//@RestController
public class RegistrationServlet extends HttpServlet {

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

    public RegistrationServlet() throws IOException {
    }

    /**
     * Метод инициализации сервлета и инициализации  Liquibase
     */
    @SneakyThrows
    @Override
    public void init() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Connection connection = null;
        Database database = null;
        Statement statement = null;
        try {
            connection = dbConnectionProvider.getConnection();
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            String sql = "CREATE SCHEMA IF NOT EXISTS liquibase";
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            database.setDefaultSchemaName("liquibase");
            Liquibase liquibase = new Liquibase(getProperty("db.changeLog"), new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
            statement.close();
        }
    }

    /**
     * Метод для выполнения регистрации игрока
     *
     * @param req
     * @param resp
     * @throws IOException
     */
//    @GetMapping("/registration")
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PlayerDTO playerDTO = objectMapper.readValue(req.getInputStream(), PlayerDTO.class);
        try {
            ValidationUtils.isEmptyOrNull(playerDTO.getName());
            ValidationUtils.size(2, 10, playerDTO.getName());
            playerService.create(playerDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getOutputStream().write(objectMapper.writeValueAsBytes(new ResponseDTO("Player created!")));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getOutputStream().write(objectMapper.writeValueAsBytes(new ResponseDTO(e.getMessage())));
            e.printStackTrace();
        }
    }
}
