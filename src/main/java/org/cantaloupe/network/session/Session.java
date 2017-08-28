package org.cantaloupe.network.session;

import java.util.UUID;

public class Session {
    private final String host;
    private final UUID   ID;

    private Session(String host, UUID ID) {
        this.host = host;
        this.ID = ID;
    }

    public static Session of(String host, UUID ID) {
        return new Session(host, ID);
    }

    public static Session of(String host) {
        return new Session(host, UUID.randomUUID());
    }

    public String getHost() {
        return this.host;
    }

    public UUID getID() {
        return this.ID;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Session)) {
            return false;
        }

        Session session = (Session) other;
        if (!session.getHost().equals(this.host) || !session.getID().equals(this.ID)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return this.host + ", " + this.ID.toString();
    }
}