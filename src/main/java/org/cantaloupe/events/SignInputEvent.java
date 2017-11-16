package org.cantaloupe.events;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cantaloupe.player.Player;

public class SignInputEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player             player;
    private final List<String>       lines;

    public SignInputEvent(Player player, List<String> lines) {
        this.player = player;
        this.lines = lines;
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
     * Gets the lines of text on the sign.
     * 
     * @return The lines
     */
    public List<String> getLines() {
        return this.lines;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}