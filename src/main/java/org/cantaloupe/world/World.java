package org.cantaloupe.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.events.WorldObjectPlaceEvent;
import org.cantaloupe.player.Player;

public class World {
    private final org.bukkit.World                 handle;
    private final DataContainer<UUID, WorldObject> worldObjects;

    protected World(org.bukkit.World handle) {
        this.handle = handle;
        this.worldObjects = DataContainer.of();
    }

    protected void load() {

    }

    protected void unload() {
        this.worldObjects.forEach((uuid, object) -> object.removeInternal());
        this.worldObjects.clear();
    }

    public boolean place(WorldObject object) {
        return this.place(object, true);
    }

    public boolean place(WorldObject object, boolean place) {
        WorldObjectPlaceEvent event = new WorldObjectPlaceEvent(this, object);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (object.getUUID() == null) {
            object.setUUID(UUID.randomUUID());
        }

        if (!event.isCancelled()) {
            if (place) {
                object.placeInternal();
            }

            this.worldObjects.put(object.getUUID(), object);

            return true;
        }

        return false;
    }

    public boolean remove(WorldObject object) {
        return this.remove(object.getUUID());
    }

    public boolean remove(UUID uuid) {
        if (this.worldObjects.containsKey(uuid)) {
            WorldObjectPlaceEvent event = new WorldObjectPlaceEvent(this, this.worldObjects.get(uuid));
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.worldObjects.get(uuid).removeInternal();
                this.worldObjects.remove(uuid);

                return true;
            }
        }

        return false;
    }

    protected void tick() {
        if (this.worldObjects.size() > 0) {
            this.worldObjects.forEach((uuid, object) -> object.tick());
        }
    }

    public void tickPlayer(Player player) {
        this.worldObjects.forEach((uuid, object) -> object.tickFor(player));
    }

    @SuppressWarnings("unchecked")
    public <T extends WorldObject> T getObject(UUID uuid) {
        return (T) this.worldObjects.get(uuid);
    }

    public Collection<WorldObject> getObjects() {
        return this.worldObjects.valueSet();
    }

    public <T extends WorldObject> Collection<T> getObjects(Class<T> clazz) {
        return this.worldObjects.branch(clazz).valueSet();
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