package org.davydov.config;

import jakarta.annotation.PostConstruct;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Data
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
            Liquibase liquibase = new Liquibase(changeLog, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

