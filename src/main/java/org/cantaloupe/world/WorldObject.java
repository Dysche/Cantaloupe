package org.cantaloupe.world;

import java.util.UUID;

import org.cantaloupe.player.Player;
import org.cantaloupe.world.location.ImmutableLocation;

public abstract class WorldObject {
    private UUID    uuid   = null;
    private boolean placed = false;
    
    protected void placeInternal() {
        this.placed = true;

        this.onPlaced();
    }

    protected void removeInternal() {
        this.onRemoved();

        this.placed = false;
    }
    
    protected void tick() {
        
    }
    
    protected void tickFor(Player player) {
        
    }
    
    protected abstract void onPlaced();

    protected abstract void onRemoved();

    public boolean isPlaced() {
        return this.placed;
    }

    protected void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public abstract ImmutableLocation getLocation();
}
