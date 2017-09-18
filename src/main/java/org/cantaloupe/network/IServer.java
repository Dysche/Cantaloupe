package org.cantaloupe.network;

import org.cantaloupe.inject.Injector;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.session.Session;

/**
 * An interface containing basic server methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IServer {
    /**
     * Starts the server.
     */
    public void start();

    /**
     * Stops the server.
     */
    public void stop();

    /**
     * Sends a packet to a client.
     * 
     * @param session
     *            The session of the client
     * @param packet
     *            The packet
     */
    public void sendPacket(Session session, IPacket packet);

    /**
     * Broadcasts a packet to all clients.
     * 
     * @param packet
     *            The packet
     */
    public void broadcast(IPacket packet);

    /**
     * Gets the injector of the server.
     * 
     * @return The injector
     */
    public Injector<? extends IConnection> getInjector();
}