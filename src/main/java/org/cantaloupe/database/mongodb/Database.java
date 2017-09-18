package org.cantaloupe.database.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoIterable;

/**
 * A class used to manage a database for a MongoDB server.
 * 
 * @author Dylan Scheltens
 *
 */
public class Database {
    private final com.mongodb.client.MongoDatabase handle;

    private Database(com.mongodb.client.MongoDatabase handle) {
        this.handle = handle;
    }

    /**
     * Creates and returns a new database.
     * 
     * @param handle
     *            The handle of the database
     * @return The database
     */
    public static Database of(com.mongodb.client.MongoDatabase handle) {
        return new Database(handle);
    }

    /**
     * Creates a new collection in the database.
     * 
     * @param collectionName
     *            The name of the collection
     */
    public void createCollection(String collectionName) {
        this.handle.createCollection(collectionName);
    }

    /**
     * Removes a collection from the database.
     * 
     * @param collectionName
     *            The name of the collection
     */
    public void removeCollection(String collectionName) {
        com.mongodb.client.MongoCollection<Document> collection = this.handle.getCollection(collectionName);

        if (collection != null) {
            collection.drop();
        }
    }

    /**
     * Checks if the database contains a collection.
     * 
     * @param collectionName
     *            The name of the collection
     * @return True if it does, false if not
     */
    public boolean hasCollection(String collectionName) {
        return this.handle.getCollection(collectionName) != null;
    }

    /**
     * Gets the collection from the database.
     * 
     * @param collectionName
     *            The name of the collection
     * @return The collection
     */
    public Collection getCollection(String collectionName) {
        return Collection.of(this.handle.getCollection(collectionName));
    }

    /**
     * Gets all collections from the database.
     * 
     * @return A list of collections
     */
    public List<Collection> getCollections() {
        List<Collection> collections = new ArrayList<Collection>();
        MongoIterable<String> names = this.handle.listCollectionNames();

        for (String name : names) {
            collections.add(this.getCollection(name));
        }

        return collections;
    }
}