package org.cantaloupe.world;

import java.util.ArrayList;
import java.util.List;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;

public class World {
    private final org.bukkit.World handle;

    protected World(org.bukkit.World handle) {
        this.handle = handle;
    }

    public org.bukkit.World toHandle() {
        return this.handle;
    }
    
    public List<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();

        this.handle.getPlayers().forEach(player -> {
            players.add(Cantaloupe.getPlayerManager().getPlayerFromHandle(player).get());
        });

        return players;
    }

    public String getName() {
        return this.handle.getName();
    }
}