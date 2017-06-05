package org.cantaloupe.service;

public interface Service {
    public default void load() {
        
    }

    public default void unload() {
        
    }

    public String getName();
}