package repository;

import model.Player;

public interface PlayerRepository {

    Player save(Player player);

    Player findById(long id);

    Player update(Player player);

    Player findByNamePassword(String name, String password);

}
