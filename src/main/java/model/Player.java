package model;

import lombok.Data;

/**
 * Data class for player
 */

@Data
public class Player {
    private long account;
    private String name, password;
    private long id;

    public Player(long id, String name, String password, long account) {
        this.account = account;
        this.name = name;
        this.id = id;
        this.password = password;
    }

    @Override
    public String toString() {
        return "id=" + this.id + ", name=" + this.name + ", account=" + this.account;
    }
}
