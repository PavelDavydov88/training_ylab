package config;

import lombok.*;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * класс который предоставляет connection
 */
//@Data
@RequiredArgsConstructor
//@NoArgsConstructor
public class DBConnectionProvider {

    private  final String url ;
    private final String username ;
    private final String password ;

    /**
     * метод создает connection
     *
     * @return connection
     */
    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DBConnectionProvider;
    }

}