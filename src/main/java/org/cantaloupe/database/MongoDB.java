package org.cantaloupe.database;

import org.bukkit.Bukkit;
import org.cantaloupe.database.mongodb.Database;
import org.cantaloupe.events.MongoConnectEvent;
import org.cantaloupe.events.MongoDisconnectEvent;

import com.google.common.base.Optional;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;

/**
 * A class used to connect to a MongoDB server.
 * 
 * @author Dylan Scheltens
 *
 */
public class MongoDB implements ServerMonitorListener {
    private final String host;
    private final int    port;
    private MongoClient  client = null;

    private MongoDB(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Creates and returns a new MongoDB client.
     * 
     * @param address
     *            The address of the server
     * @return The server
     */
    public static MongoDB of(ServerAddress address) {
        return new MongoDB(address.getHost(), address.getPort());
    }

    /**
     * Creates and returns a new MongoDB client.
     * 
     * @param host
     *            The host of the server
     * @param port
     *            The port of the server
     * @return The server
     */
    public static MongoDB of(String host, int port) {
        return new MongoDB(host, port);
    }

    /**
     * Creates and returns a new MongoDB client.
     * 
     * @param host
     *            The host of the server
     * @return The server
     */
    public static MongoDB of(String host) {
        return new MongoDB(host, -1);
    }

    /**
     * Creates and returns a new MongoDB client.
     * 
     * @param port
     *            The port of the server
     * @return The server
     */
    public static MongoDB of(int port) {
        return new MongoDB(null, port);
    }

    /**
     * Creates and returns a new MongoDB client.
     * 
     * @return The server
     */
    public static MongoDB of() {
        return new MongoDB(null, -1);
    }

    @Override
    public void serverHearbeatStarted(ServerHeartbeatStartedEvent arg0) {

    }

    @Override
    public void serverHeartbeatFailed(ServerHeartbeatFailedEvent arg0) {

    }

    @Override
    public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent arg0) {

    }

    /**
     * Opens a connection to the MongoDB server.
     */
    public void connect() {
        MongoClientOptions clientOptions = new MongoClientOptions.Builder().addServerMonitorListener(this).build();

        if (this.host != null && this.port != -1) {
            this.client = new MongoClient(new ServerAddress(this.host, this.port), clientOptions);
        } else if (this.port == -1) {
            this.client = new MongoClient(new ServerAddress(this.host), clientOptions);
        } else if (this.host == null) {
            this.client = new MongoClient(new ServerAddress("localhost", this.port), clientOptions);
        }

        Bukkit.getServer().getPluginManager().callEvent(new MongoConnectEvent(this));
    }

    /**
     * Closes the connection to the MongoDB server.
     */
    public void disconnect() {
        this.client.close();

        Bukkit.getServer().getPluginManager().callEvent(new MongoDisconnectEvent(this));
    }

    /**
     * Drops a database.
     * 
     * @param databaseName
     *            The name of the database
     */
    public void dropDatabase(String databaseName) {
        this.client.dropDatabase(databaseName);
    }

    /**
     * Gets a database from the server.
     * 
     * @param databaseName
     *            The name of the database
     * @return An optional containing the database if it's present, an empty
     *         optional if not
     */
    public Optional<Database> getDatabase(String databaseName) {
        return Optional.of(Database.of(this.client.getDatabase(databaseName)));
    }

    /**
     * Gets the address of the server.
     * 
     * @return The address
     */
    public ServerAddress getAddress() {
        return this.client.getAddress();
    }
}