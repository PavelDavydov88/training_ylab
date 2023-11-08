package org.davydov.repository;

import lombok.RequiredArgsConstructor;
import org.davydov.config.DBConnectionProvider;
import org.example.auditstarter.repository.AuditRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO for Audit operations
 */
@Primary
@Repository
@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {

    private final DBConnectionProvider dbConnectionProvider;
    private static final String INSERT_AUDIT = """
            INSERT INTO wallet."audit" ("id", "id_player", "operation") 
            VALUES (nextval( 'wallet.sequence_audit'), ?, ?)""";
    private static final String SELECT_FIND_AUDIT = """
            select * from wallet."audit" where "id_player" = ?""";

    /**
     * Метод сохраняет действия игрока
     *
     * @param idPlayer    ID игрока
     * @param historyText действие игрока
     * @throws SQLException
     */
    @Override
    public void save(long idPlayer, String historyText) throws SQLException {
        String historyWithDate = historyText + ", time = " + new Date();
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUDIT)) {
            preparedStatement.setLong(1, idPlayer);
            preparedStatement.setString(2, historyWithDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращает все действия игрока
     *
     * @param id ID игрока
     * @return список действий игрока
     * @throws SQLException
     */
    @Override
    public List<String> findAllById(long id) throws Exception {
        List<String> listHistory = new ArrayList<>();
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FIND_AUDIT)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String operation = resultSet.getString("operation");
                    listHistory.add(operation);
                }
            }
            return listHistory;
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
}
