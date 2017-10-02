package org.cantaloupe.world;

import java.util.UUID;

import org.cantaloupe.player.Player;
import org.cantaloupe.world.location.ImmutableLocation;

public abstract class WorldObject {
    private UUID    uuid   = null;
    private boolean placed = false, dirty = false;

    public void place() {
        this.getWorld().place(this);
    }

    public void remove() {
        this.getWorld().remove(this);
    }

    protected void placeInternal() {
        this.placed = true;

        this.onPlaced();
    }

    protected void removeInternal(RemoveCause cause) {
        this.onRemoved(cause);
        this.onRemoved();

        this.placed = false;
    }

    protected void tick() {

    }

    protected void tickFor(Player player) {

    }

    protected void markDirty() {
        this.dirty = true;
    }

    protected void onPlaced() {
        
    }

    protected void onRemoved(RemoveCause cause) {
        
    }
    
    protected void onRemoved() {
        
    }
    
    public boolean isPlaced() {
        return this.placed;
    }

    protected boolean isDirty() {
        return this.dirty;
    }

    protected void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public abstract ImmutableLocation getLocation();

    public World getWorld() {
        return this.getLocation().getWorld();
    }
}