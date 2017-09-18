package org.cantaloupe.service.services;

import java.util.Optional;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.database.MongoDB;
import org.cantaloupe.service.IService;

import com.mongodb.ServerAddress;

/**
 * A service used to manage MongoDB connections.
 * 
 * @author Dylan Scheltens
 *
 */
public class MongoService implements IService {
    private DataContainer<String, MongoDB> connections = null;

    @Override
    public void load() {
        this.connections = DataContainer.of();
    }

    @Override
    public void unload() {
        for (MongoDB connection : this.connections.valueSet()) {
            connection.disconnect();
        }

        this.connections.clear();
        this.connections = null;
    }

    /**
     * Creates and returns a connection to a MongoDB server.
     * 
     * @param name
     *            The name of the server
     * @param address
     *            The address of the server
     * 
     * @return The server
     */
    public MongoDB connect(String name, ServerAddress address) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.of(address);
            this.connections.put(name, connection);

            return connection;
        }
    }

    /**
     * Creates and returns a connection to a MongoDB server.
     * 
     * @param name
     *            The name of the server
     * @param host
     *            The host of the server
     * @param port
     *            The port of the server
     * 
     * @return The server
     */
    public MongoDB connect(String name, String host, int port) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.of(host, port);
            this.connections.put(name, connection);

            return connection;
        }
    }

    /**
     * Creates and returns a connection to a MongoDB server.
     * 
     * @param name
     *            The name of the server
     * @param host
     *            The host of the server
     * 
     * @return The server
     */
    public MongoDB connect(String name, String host) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.of(host);
            this.connections.put(name, connection);

            return connection;
        }
    }

    /**
     * Creates and returns a connection to a MongoDB server.
     * 
     * @param name
     *            The name of the server
     * @param port
     *            The port of the server
     * 
     * @return The server
     */
    public MongoDB connect(String name, int port) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.of(port);
            this.connections.put(name, connection);

            return connection;
        }
    }

    /**
     * Creates and returns a connection to a MongoDB server.
     * 
     * @param name
     *            The name of the server
     * 
     * @return The server
     */
    public MongoDB connect(String name) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.of();
            connection.connect();

            this.connections.put(name, connection);

            return connection;
        }
    }

    /**
     * Closes the connection to a MongoDB server.
     * 
     * @param name
     *            The name of the server
     */
    public void disconnect(String name) {
        if (this.connections.containsKey(name)) {
            this.connections.get(name).disconnect();
            this.connections.remove(name);
        }
    }

    /**
     * Checks if a server is connected.
     * 
     * @param name
     *            The name of the server
     * 
     * @return True if it is, false if not
     */
    public boolean isConnected(String name) {
        return this.connections.containsKey(name);
    }

    /**
     * Gets a server from the service.
     * 
     * @param name
     *            The name of a server
     * @return An optional containing the server if it's present, an empty
     *         optional if not
     */
    public Optional<MongoDB> getConnection(String name) {
        return Optional.ofNullable(this.connections.get(name));
    }

    @Override
    public String getName() {
        return "mongo";
    }
}