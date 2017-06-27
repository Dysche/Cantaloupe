package org.cantaloupe.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.ScheduleService;

public class WorldManager {
    private final Map<String, World> worlds;

    public WorldManager() {
        this.worlds = new HashMap<String, World>();
    }

    public void load() {
        Bukkit.getWorlds().forEach(world -> {
            this.registerWorld(world);
        });

        Cantaloupe.getServiceManager().provide(ScheduleService.class).repeat("worldTicker", new Runnable() {
            @Override
            public void run() {
                worlds.forEach((name, world) -> {
                    world.tick();
                });
            }
        }, 0L);
    }

    public void unload() {
        this.worlds.forEach((name, world) -> world.unload());
        this.worlds.clear();

        Cantaloupe.getServiceManager().provide(ScheduleService.class).cancel("worldTicker");
    }

    public void registerWorld(org.bukkit.World handle) {
        World world = new World(handle);
        world.load();

        this.worlds.put(world.getName(), world);
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