package org.cantaloupe.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
    private final String host;
    private final int    port;
    private MongoClient  client = null;

    private MongoDB(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static MongoDB create(String host, int port) {
        return new MongoDB(host, port);
    }

    public void connect() {
        this.client = new MongoClient(this.host, this.port);
    }

    public void disconnect() {
        this.client.close();
    }
    
    public void dropDatabase(String databaseName) {
        this.client.dropDatabase(databaseName);
    }

    public MongoDatabase getDatabase(String databaseName) {
        return this.client.getDatabase(databaseName);
    }
}