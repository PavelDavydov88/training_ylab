package org.davydov.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class for player
 */

@Data
@AllArgsConstructor
public class Player {
    private long id;
    private String name;
    private String password;
    private long account;

    @Override
    public String toString() {
        return "id=" + this.id + ", name=" + this.name + ", account=" + this.account;
    }
}
