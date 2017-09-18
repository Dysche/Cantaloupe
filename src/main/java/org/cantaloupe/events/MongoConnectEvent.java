package org.cantaloupe.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cantaloupe.database.MongoDB;

public class MongoConnectEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final MongoDB            client;

    public MongoConnectEvent(MongoDB client) {
        this.client = client;
    }

    /**
     * Gets the MongoDB client.
     * 
     * @return The client
     */
    public MongoDB getClient() {
        return this.client;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}