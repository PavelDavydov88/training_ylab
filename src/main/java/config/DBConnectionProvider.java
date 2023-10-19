package config;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *класс который предоставляет connection
 */
@RequiredArgsConstructor
public class DBConnectionProvider {

    private final String url;
    private final String username;
    private final String password;

    /**
     * метод создает connection
     * @return connection
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}