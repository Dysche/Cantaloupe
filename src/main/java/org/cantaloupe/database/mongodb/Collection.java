package org.cantaloupe.database.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.util.DatabaseUtils;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;

/**
 * A class used to manage data for a MongoDB database.
 * 
 * @author Dylan Scheltens
 *
 */
public class Collection {
    private final com.mongodb.client.MongoCollection<Document> handle;

    private Collection(com.mongodb.client.MongoCollection<Document> handle) {
        this.handle = handle;
    }

    /**
     * Creates and returns a new collection.
     * 
     * @param handle
     *            The handle of the collection
     * @return The collection
     */
    public static Collection of(com.mongodb.client.MongoCollection<Document> handle) {
        return new Collection(handle);
    }

    /**
     * Inserts an entry in the collection.
     * 
     * @param key
     *            The key of the entry
     * @param container
     *            The data of the entry
     */
    public void insert(String key, DataContainer<String, Object> container) {
        this.handle.insertOne(DatabaseUtils.containerToDocument(key, container));
    }

    /**
     * Inserts or updates and entry in the collection.
     * 
     * @param key
     *            The key of the entry
     * @param container
     *            The data of the entry
     */
    public void upsert(String key, DataContainer<String, Object> container) {
        Document query = new Document();
        query.append("{documentKey}", key);

        this.handle.updateOne(query, new Document("$set", DatabaseUtils.containerToDocument(key, container)), new UpdateOptions().upsert(true));
    }

    /**
     * Updates an entry in the collection.
     * 
     * @param key
     *            The key of the entry
     * @param container
     *            The data of the entry
     */
    public void update(String key, DataContainer<String, Object> container) {
        Document query = new Document();
        query.append("{documentKey}", key);

        this.handle.updateOne(query, new Document("$set", DatabaseUtils.containerToDocument(key, container)));
    }

    /**
     * Deletes an entry from the collection.
     * 
     * @param key
     *            The key of the entry
     */
    public void delete(String key) {
        Document query = new Document();
        query.append("{documentKey}", key);

        this.handle.deleteOne(query);
    }

    /**
     * Deletes multiple entries from the collection with the same key.
     * 
     * @param key
     *            The key of the entries
     */
    public void deleteMultiple(String key) {
        Document query = new Document();
        query.append("{documentKey}", key);

        this.handle.deleteMany(query);
    }

    /**
     * Returns one or more entries from the collection.
     * 
     * @param key
     *            The key of the entry/entries
     * @return A list of entries
     */
    public List<DataContainer<String, Object>> retrieve(String key) {
        ArrayList<DataContainer<String, Object>> results = new ArrayList<DataContainer<String, Object>>();

        Document query = new Document();
        query.append("{documentKey}", key);

        FindIterable<Document> iterable = this.handle.find(query);
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                results.add(DatabaseUtils.documentToContainer(document));
            }
        });

        return results;
    }

    /**
     * Returns all entries in this collection.
     * 
     * @return A list of entries
     */
    public List<DataContainer<String, Object>> retrieve() {
        ArrayList<DataContainer<String, Object>> results = new ArrayList<DataContainer<String, Object>>();

        FindIterable<Document> iterable = this.handle.find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                results.add(DatabaseUtils.documentToContainer(document));
            }
        });

        return results;
    }

    /**
     * Returns the amount of entries in the collection by entry key.
     * 
     * @param key
     *            The key of the entry/entries
     * @return The amount
     */
    public long count(String key) {
        Document query = new Document();
        query.append("{documentKey}", key);

        return this.handle.count(query);
    }

    /**
     * Returns the amount of entries in the collection.
     * 
     * @return The amount
     */
    public long count() {
        return this.handle.count();
    }
}