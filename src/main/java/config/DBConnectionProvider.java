package config;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;

@RequiredArgsConstructor
public class DBConnectionProvider {

    private final String url;
    private final String username;
    private final String password;

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}