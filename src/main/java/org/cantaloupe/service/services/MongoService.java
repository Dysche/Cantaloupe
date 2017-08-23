package org.cantaloupe.service.services;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.database.MongoDB;
import org.cantaloupe.service.Service;

import com.mongodb.ServerAddress;

public class MongoService implements Service {
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

    public MongoDB connect(String name, ServerAddress address) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.create(address);
            this.connections.put(name, connection);

            return connection;
        }
    }

    public MongoDB connect(String name, String host, int port) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.create(host, port);
            this.connections.put(name, connection);

            return connection;
        }
    }

    public MongoDB connect(String name, String host) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.create(host);
            this.connections.put(name, connection);

            return connection;
        }
    }

    public MongoDB connect(String name, int port) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.create(port);
            this.connections.put(name, connection);

            return connection;
        }
    }

    public MongoDB connect(String name) {
        if (this.connections.containsKey(name)) {
            return this.connections.get(name);
        } else {
            MongoDB connection = MongoDB.create();
            connection.connect();

            this.connections.put(name, connection);

            return connection;
        }
    }

    public void disconnect(String name) {
        if (this.connections.containsKey(name)) {
            this.connections.get(name).disconnect();
            this.connections.remove(name);
        }
    }

    public boolean isConnected(String name) {
        return this.connections.containsKey(name);
    }

    public MongoDB getConnection(String name) {
        return this.connections.get(name);
    }

    @Override
    public String getName() {
        return "mongo";
    }
}