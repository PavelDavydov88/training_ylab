package repository;

import model.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerRepositoryImpl implements PlayerRepository {

    private static final AtomicLong idPlayer = new AtomicLong(0);
    private static final Map<Long, Player> mapPlayers = new ConcurrentHashMap<>();

    @Override
    public Player save(Player inputPlayer) {
        Player newPlayer = new Player(inputPlayer.getName(), inputPlayer.getPassword(), idPlayer.incrementAndGet());
        mapPlayers.put(newPlayer.getId(), newPlayer);
        return newPlayer;
    }

    @Override
    public Player findById(long id) {

        return mapPlayers.get(id);
    }

    @Override
    public Player findByNamePassword(String name, String password) {
        return mapPlayers.values().stream().filter(a -> a.getName().equals(name) && a.getPassword().equals(password)).findFirst().orElse(null);
    }

    @Override
    public Player update(Player updatePlayer) {
        Player player = mapPlayers.get(updatePlayer.getId());
        player.setAccount(updatePlayer.getAccount());
        return player;
    }
}
