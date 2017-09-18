package org.cantaloupe.network;

import org.cantaloupe.inject.Injector;
import org.cantaloupe.network.packet.IPacket;

/**
 * An interface containing basic connection methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IConnection {
    /**
     * Opens a connection.
     */
    public void open();

    /**
     * Closes the connection.
     */
    public void close();

    /**
     * Closes the connection externally.
     */
    public void closeExt();

    /**
     * Sends a packet.
     * 
     * @param packet
     *            The packet.
     */
    public void sendPacket(IPacket packet);

    /**
     * Gets the injector of the connection.
     * 
     * @return The injector
     */
    public Injector<? extends IConnection> getInjector();
}