package org.cantaloupe.network.packet;

import org.cantaloupe.network.IConnection;

/**
 * An interface containing basic packet listener methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IPacketListener {
    /**
     * Handles a packet has been received from a connection.
     * 
     * @param connection
     *            The connection
     * @param packet
     *            The packet
     */
    public void onPacketRecieved(IConnection connection, IPacket packet);
}