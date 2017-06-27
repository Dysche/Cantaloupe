package org.cantaloupe.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;

public class WorldObjectPlaceEvent extends Event implements Cancellable {
    private static final HandlerList handlers  = new HandlerList();
    private final World              world;
    private final WorldObject        worldObject;
    private boolean                  cancelled = false;

    public WorldObjectPlaceEvent(World world, WorldObject worldObject) {
        this.world = world;
        this.worldObject = worldObject;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public World getWorld() {
        return this.world;
    }

    public WorldObject getWorldObject() {
        return this.worldObject;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}