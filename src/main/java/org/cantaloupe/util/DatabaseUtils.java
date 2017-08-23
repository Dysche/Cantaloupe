package org.cantaloupe.util;

import org.bson.Document;
import org.cantaloupe.data.DataContainer;

public class DatabaseUtils {
    public static Document containerToDocument(String documentKey, DataContainer<String, Object> container) {
        Document document = new Document();
        document.append("{documentKey}", documentKey);

        container.forEach((key, value) -> {
            document.append(key, value);
        });

        return document;
    }

    public static DataContainer<String, Object> documentToContainer(Document document) {
        DataContainer<String, Object> container = DataContainer.of();

        document.forEach((key, value) -> {
            container.put(key, value);
        });
        
        container.remove("{documentKey}");

        return container;
    }
}