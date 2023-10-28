package repository;

import model.Player;
import model.PlayerDTO;

import java.sql.SQLException;

public interface PlayerRepository {

    void save(Player player) throws SQLException;

    Player findById(long id) throws SQLException;

    Player update(Player player) throws SQLException;

    Player findByNamePassword(PlayerDTO dto) throws SQLException;

}
