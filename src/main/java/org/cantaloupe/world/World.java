package org.cantaloupe.world;

import java.util.ArrayList;
import java.util.List;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.user.User;

public class World {
    private final org.bukkit.World handle;

    protected World(org.bukkit.World handle) {
        this.handle = handle;
    }

    public List<User> getPlayers() {
        ArrayList<User> users = new ArrayList<User>();

        this.handle.getPlayers().forEach(player -> {
            users.add(Cantaloupe.getUserManager().getUserFromHandle(player).get());
        });

        return users;
    }

    public org.bukkit.World getHandle() {
        return this.handle;
    }

    public String getName() {
        return this.handle.getName();
    }
}