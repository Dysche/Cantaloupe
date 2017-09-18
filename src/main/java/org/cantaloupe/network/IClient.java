package org.cantaloupe.network;

import org.cantaloupe.inject.Injector;

/**
 * An interface containing basic client methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IClient {
    /**
     * Opens a connection to the server.
     */
    public void connect();

    /**
     * Closes the connection to the server.
     */
    public void disconnect();

    /**
     * Gets the connection.
     * 
     * @return The connection.
     */
    public IConnection getConnection();

    /**
     * Gets the injector of the client.
     * 
     * @return The injector
     */
    public Injector<? extends IConnection> getInjector();
}