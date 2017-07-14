package org.cantaloupe.database.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoIterable;

public class Database {
    private final com.mongodb.client.MongoDatabase handle;

    private Database(com.mongodb.client.MongoDatabase handle) {
        this.handle = handle;
    }

    public static Database of(com.mongodb.client.MongoDatabase handle) {
        return new Database(handle);
    }

    public void createCollection(String collectionName) {
        this.handle.createCollection(collectionName);
    }

    public void removeCollection(String collectionName) {
        com.mongodb.client.MongoCollection<Document> collection = this.handle.getCollection(collectionName);

        if (collection != null) {
            collection.drop();
        }
    }

    public boolean isCollection(String collectionName) {
        return this.handle.getCollection(collectionName) != null;
    }

    public Collection getCollection(String collectionName) {
        return Collection.of(this.handle.getCollection(collectionName));
    }

    public List<Collection> getCollections() {
        List<Collection> collections = new ArrayList<Collection>();
        MongoIterable<String> names = this.handle.listCollectionNames();

        for (String name : names) {
            collections.add(this.getCollection(name));
        }

        return collections;
    }
}