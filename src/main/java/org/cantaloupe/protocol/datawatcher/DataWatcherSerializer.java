package org.cantaloupe.protocol.datawatcher;

public class DataWatcherSerializer<T> {
    private final char                           fieldName;

    protected DataWatcherSerializer(char fieldName) {
        this.fieldName = fieldName;
    }

    protected char getFieldName() {
        return this.fieldName;
    }
}
