package repository;

import java.sql.SQLException;

public interface AuthRepository {
    void save(String token) throws SQLException;

    String find(String token) throws SQLException;

    void delete(String token) throws SQLException;
}
