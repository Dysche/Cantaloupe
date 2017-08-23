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

public class MongoDB implements ServerMonitorListener {
    private final String host;
    private final int    port;
    private MongoClient  client = null;

    private MongoDB(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static MongoDB create(ServerAddress address) {
        return new MongoDB(address.getHost(), address.getPort());
    }

    public static MongoDB create(String host, int port) {
        return new MongoDB(host, port);
    }

    public static MongoDB create(String host) {
        return new MongoDB(host, -1);
    }

    public static MongoDB create(int port) {
        return new MongoDB(null, port);
    }

    public static MongoDB create() {
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

    public void disconnect() {
        this.client.close();

        Bukkit.getServer().getPluginManager().callEvent(new MongoDisconnectEvent(this));
    }

    public void dropDatabase(String databaseName) {
        this.client.dropDatabase(databaseName);
    }

    public Optional<Database> getDatabase(String databaseName) {
        return Optional.of(Database.of(this.client.getDatabase(databaseName)));
    }

    public ServerAddress getAddress() {
        return this.client.getAddress();
    }
}