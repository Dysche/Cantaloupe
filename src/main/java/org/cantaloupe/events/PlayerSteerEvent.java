package org.cantaloupe.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cantaloupe.player.Player;

public class PlayerSteerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player             player;
    private final boolean            shift, space;
    private final float              forward, side;

    public PlayerSteerEvent(Player player, boolean shift, boolean space, float forward, float side) {
        this.player = player;
        this.shift = shift;
        this.space = space;
        this.forward = forward;
        this.side = side;
    }

    /**
     * Gets the player.
     * 
     * @return The player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets whether or not shift is pressed.
     * 
     * @return True if it is, false if not
     */
    public boolean getShift() {
        return this.shift;
    }

    /**
     * Gets whether or not space is pressed.
     * 
     * @return True if it is, false if not
     */
    public boolean getSpace() {
        return this.space;
    }

    /**
     * Gets the forward amount.
     * 
     * @return The amount
     */
    public float getForward() {
        return this.forward;
    }

    /**
     * Gets the side amount.
     * 
     * @return The amount
     */
    public float getSide() {
        return this.side;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}