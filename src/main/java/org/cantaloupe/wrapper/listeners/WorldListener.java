package org.cantaloupe.wrapper.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.cantaloupe.Cantaloupe;

public class WorldListener implements Listener {
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        Cantaloupe.getWorldManager().registerWorld(event.getWorld());
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        Cantaloupe.getWorldManager().unregisterWorld(event.getWorld().getName());
    }
}