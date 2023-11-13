package org.davydov.config;

import jakarta.annotation.PostConstruct;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Класс для инициализации liquibase
 */
@Data
@Slf4j
@Component
public class LiquibaseInit {

    @Value("${db.changeLog}")
    private String changeLog;
    @Value("${db.url}")
    private String url;
    @Value("${db.user}")
    private String username;
    @Value("${db.password}")
    private String password;
    private static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS liquibase";


    /**
     * Метод для инициализации liquibase
     */
    @PostConstruct
    public void initLiquibase() {
        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
        dbConnectionProvider.setPassword(password);
        dbConnectionProvider.setUrl(url);
        dbConnectionProvider.setUsername(username);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = dbConnectionProvider.getConnection();
             Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_SCHEMA);
            database.setDefaultSchemaName("liquibase");
            Liquibase liquibase = new Liquibase(changeLog, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            log.info("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

