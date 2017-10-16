package org.cantaloupe.util;

import org.bson.Document;
import org.cantaloupe.data.DataContainer;

public class DatabaseUtils {
    @SuppressWarnings("unchecked")
    public static Document containerToDocument(String documentKey, DataContainer<String, Object> container) {
        Document document = new Document();

        if (documentKey != null) {
            document.append("{documentKey}", documentKey);
        }

        container.forEach((key, value) -> {
            if (value instanceof DataContainer) {
                document.append(key, containerToDocument(null, (DataContainer<String, Object>) value));
            } else {
                document.append(key, value);
            }
        });

        return document;
    }

    public static DataContainer<String, Object> documentToContainer(Document document) {
        DataContainer<String, Object> container = DataContainer.of();

        document.forEach((key, value) -> {
            if (value instanceof Document) {
                container.put(key, documentToContainer((Document) value));
            } else {
                container.put(key, value);
            }
        });

        container.remove("{documentKey}");

        return container;
    }
}