package in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.DBConnectionProvider;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Size;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.PlayerDTO;
import model.ResponseDTO;
import org.hibernate.validator.constraints.NotEmpty;
import repository.*;
import service.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Set;

import static config.PropertyUtils.getProperty;

@Slf4j
@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(getProperty("db.url"), getProperty("db.user"), getProperty("db.password"));
    PlayerRepository playerRepository = new PlayerRepositoryImpl(dbConnectionProvider);
    AuthRepository authRepository = new AuthRepositoryImpl(dbConnectionProvider);
    TransactionRepository transactionRepository = new TransactionRepositoryImpl(dbConnectionProvider);
    HistoryCreditDebitRepository historyCreditDebitRepository = new HistoryCreditDebitRepositoryImpl(dbConnectionProvider);
    AuditRepository auditRepository = new AuditRepositoryImpl(dbConnectionProvider);

    AuditService auditService = new AuditServiceImpl(auditRepository);
    AuthService authService = new AuthServiceImpl(playerRepository, authRepository, auditRepository);
    TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
    PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitRepository, auditService);
    ObjectMapper objectMapper = new ObjectMapper();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();


    public RegistrationServlet() throws IOException {
    }

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


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Validator validator = factory.getValidator();
        PlayerDTO playerDTO = objectMapper.readValue(req.getInputStream(), PlayerDTO.class);
        try {
            Set<ConstraintViolation<PlayerDTO>> validate = validator.validate(playerDTO);
            validate.stream().findFirst().ifPresent(e -> {throw new RuntimeException(e.getMessage());});
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