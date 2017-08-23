package org.cantaloupe.database.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.util.DatabaseUtils;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;

public class Collection {
    private final com.mongodb.client.MongoCollection<Document> handle;

    private Collection(com.mongodb.client.MongoCollection<Document> handle) {
        this.handle = handle;
    }

    public static Collection of(com.mongodb.client.MongoCollection<Document> handle) {
        return new Collection(handle);
    }

    public void insert(String key, DataContainer<String, Object> container) {
        this.handle.insertOne(DatabaseUtils.containerToDocument(key, container));
    }

    public void upsert(String key, DataContainer<String, Object> container) {
        Document query = new Document();
        query.append("{documentKey}", key);

        this.handle.updateOne(query, new Document("$set", DatabaseUtils.containerToDocument(key, container)), new UpdateOptions().upsert(true));
    }

    public void update(String key, DataContainer<String, Object> container) {
        Document query = new Document();
        query.append("{documentKey}", key);

        this.handle.updateOne(query, new Document("$set", DatabaseUtils.containerToDocument(key, container)));
    }
    
    public void delete(String key) {
        Document query = new Document();
        query.append("{documentKey}", key);
        
        this.handle.deleteOne(query);
    }
    
    public void deleteMultiple(String key) {
        Document query = new Document();
        query.append("{documentKey}", key);
        
        this.handle.deleteMany(query);
    }

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

    public long count(String key) {
        Document query = new Document();
        query.append("{documentKey}", key);

        return this.handle.count(query);
    }

    public long count() {
        return this.handle.count();
    }
}