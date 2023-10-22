package repository;

import java.sql.SQLException;
import java.util.Optional;

public interface AuthRepository {
    void save(String token) throws SQLException;

    Optional<String> find(String token) throws SQLException;

    void delete(String token) throws SQLException;
}
