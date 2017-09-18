package org.cantaloupe.service;

/**
 * An interface containing basic service methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IService {
    /**
     * Loads the service.
     */
    public default void load() {

    }

    /**
     * Unloads the service.
     */
    public default void unload() {

    }

    /**
     * Gets the name of the service.
     * 
     * @return The name
     */
    public String getName();
}