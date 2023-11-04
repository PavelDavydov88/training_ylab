package org.davydov.repository;

import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;

import java.sql.SQLException;

public interface PlayerRepository {

    void save(Player player) throws SQLException;

    Player findById(long id) throws SQLException;

    Player update(Player player) throws SQLException;

    Player findByNamePassword(PlayerDTO dto) throws SQLException;

}
