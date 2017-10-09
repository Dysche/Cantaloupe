package org.cantaloupe.world;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cantaloupe.player.Player;
import org.cantaloupe.world.location.ImmutableLocation;

public abstract class WorldObject {
    private UUID              uuid     = null;
    private boolean           placed   = false, dirty = false;

    private WorldObject       parent   = null;
    private List<WorldObject> children = null;

    public void place() {
        this.getWorld().place(this);
    }

    public void remove() {
        this.getWorld().remove(this);

        if (this.parent != null) {
            this.parent.children.remove(this);
        }
    }

    public void addChild(WorldObject child) {
        this.addChild(child, false);
    }

    public void addChild(WorldObject child, boolean place) {
        child.parent = this;
        
        if (place) {
            if (this.placed) {
                child.onPlaced();
            }
        }

        if (this.children == null) {
            this.children = new ArrayList<WorldObject>();
        }

        this.children.add(child);
    }

    protected void placeInternal() {
        this.placed = true;

        this.onPlaced();

        if (this.children != null) {
            for (WorldObject child : this.children) {
                child.place();
            }
        }
    }

    protected void removeInternal(RemoveCause cause) {
        this.onRemoved(cause);
        this.onRemoved();

        if (this.children != null) {
            for (WorldObject child : this.children) {
                if (cause == RemoveCause.GENERAL) {
                    child.remove();
                }
            }
        }

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

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean hasChildren() {
        return this.children != null && this.children.size() != 0;
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

    public WorldObject getParent() {
        return this.parent;
    }

    public List<WorldObject> getChildren() {
        return this.children;
    }
}