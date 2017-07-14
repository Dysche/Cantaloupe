package org.cantaloupe.database.mongodb;

import org.bson.Document;

public class Collection {
    private final com.mongodb.client.MongoCollection<Document> handle;

    private Collection(com.mongodb.client.MongoCollection<Document> handle) {
        this.handle = handle;
    }

    public static Collection of(com.mongodb.client.MongoCollection<Document> handle) {
        return new Collection(handle);
    }
}
