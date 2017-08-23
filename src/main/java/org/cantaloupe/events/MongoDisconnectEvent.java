package org.cantaloupe.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cantaloupe.database.MongoDB;

public class MongoDisconnectEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final MongoDB            driver;

    public MongoDisconnectEvent(MongoDB driver) {
        this.driver = driver;
    }

    public MongoDB getDriver() {
        return this.driver;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}