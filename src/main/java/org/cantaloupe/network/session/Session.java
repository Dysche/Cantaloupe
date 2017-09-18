package org.cantaloupe.network.session;

import java.util.UUID;

/**
 * A class containing session information.
 * 
 * @author Dylan Scheltens
 *
 */
public class Session {
    private final String host;
    private final UUID   ID;

    private Session(String host, UUID ID) {
        this.host = host;
        this.ID = ID;
    }

    /**
     * Creates and returns a new session.
     * 
     * @param host
     *            The session host
     * @param ID
     *            The session ID
     * @return The session
     */
    public static Session of(String host, UUID ID) {
        return new Session(host, ID);
    }

    /**
     * Creates and returns a new session.
     * 
     * @param host
     *            The session host
     * @return The session
     */
    public static Session of(String host) {
        return new Session(host, UUID.randomUUID());
    }

    /**
     * Gets the host of the session.
     * 
     * @return The host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Gets the ID of the session.
     * 
     * @return The ID
     */
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