package org.davydov.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.davydov.config.DBConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
@Testcontainers
public class RepositoryLiquibaseInit {

    @Value("${db.changeLog}")
    private String changeLog;
    @Container
    private final PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

//    @Container
//    private final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
//            "postgres:14.7-alpine");

    @Autowired
    DBConnectionProvider dbConnectionProvider;
    Connection connection;
    Statement statement;

    public void setUp() throws SQLException, LiquibaseException {
        dbConnectionProvider.setUsername(postgresContainer.getUsername());
        dbConnectionProvider.setPassword(postgresContainer.getPassword());
        dbConnectionProvider.setUrl(postgresContainer.getJdbcUrl());
        connection = DriverManager
                .getConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        String sql = "CREATE SCHEMA IF NOT EXISTS liquibase";
        statement = connection.createStatement();
        statement.executeUpdate(sql);
        database.setDefaultSchemaName("liquibase");
        Liquibase liquibase = new Liquibase(changeLog, new ClassLoaderResourceAccessor(), database);
        liquibase.update();
        connection = DriverManager
                .getConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
        statement = connection.createStatement();
    }
}