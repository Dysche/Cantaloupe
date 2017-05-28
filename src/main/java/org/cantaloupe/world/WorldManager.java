package org.cantaloupe.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

public class WorldManager {
    private final Map<String, World> worlds;

    public WorldManager() {
        this.worlds = new HashMap<String, World>();
    }

    public void load() {
        Bukkit.getWorlds().forEach(world -> {
            this.registerWorld(world);
        });
    }

    public void unload() {
        this.worlds.keySet().forEach(this::unregisterWorld);
    }

    public void registerWorld(org.bukkit.World world) {
        this.worlds.put(world.getName(), new World(world));
    }

    public void unregisterWorld(String name) {
        this.worlds.remove(name);
    }
    
    public World getWorld(String name) {
        return this.worlds.get(name);
    }
    
    public World getWorldFromHandle(org.bukkit.World handle) {
        return this.worlds.get(handle.getName());
    }

    public Collection<World> getWorlds() {
        return this.worlds.values();
    }
}
