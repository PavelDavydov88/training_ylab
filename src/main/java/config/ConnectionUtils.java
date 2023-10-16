package config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {
    private static ConnectionUtils INSTANCE;

    private ConnectionUtils() {
    }

    public static ConnectionUtils getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionUtils();
        }
        return INSTANCE;
    }

    public static Connection getConnection() throws IOException, SQLException {
        Connection connection = null;
        final String URL = PropertyUtils.getProperty("db.host");
        final String USER_NAME = PropertyUtils.getProperty("db.user");
        final String PASSWORD = PropertyUtils.getProperty("db.password");
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            return connection;
        } catch (SQLException e) {
            connection.close();
            System.out.println("Got SQL Exception " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}


