package org.cantaloupe.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cantaloupe.entity.FakeEntity;
import org.cantaloupe.player.Player;
import org.joml.Vector3d;

public class PlayerInteractAtFakeEntityEvent extends Event implements Cancellable {
    private static final HandlerList handlers  = new HandlerList();
    private final Player             player;
    private final FakeEntity         entity;
    private final Vector3d           clickedPosition;
    private boolean                  cancelled = false;

    public PlayerInteractAtFakeEntityEvent(Player player, FakeEntity entity, Vector3d clickedPosition) {
        this.player = player;
        this.entity = entity;
        this.clickedPosition = clickedPosition;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public FakeEntity getRightClicked() {
        return this.entity;
    }

    public Vector3d getClickedPosition() {
        return this.clickedPosition;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}