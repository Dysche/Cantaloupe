package org.cantaloupe.entity;

import java.util.Collection;

import org.cantaloupe.data.DataContainer;

/**
 * A class used to store "fake" entities.
 * 
 * @author Dylan Scheltens
 *
 */
public class FakeEntityContainer {
    private static DataContainer<Integer, FakeEntity> entities = DataContainer.of();

    /**
     * Adds an entity to the container.
     * 
     * @param entity
     *            The entity
     */
    public static void addEntity(FakeEntity entity) {
        entities.put(entity.getEntityID(), entity);
    }

    /**
     * Removes an entity from the container.
     * 
     * @param entity
     *            The entity
     */
    public static void removeEntity(FakeEntity entity) {
        removeEntity(entity.getEntityID());
    }

    /**
     * Removes an entity from the container.
     * 
     * @param entityID
     *            The ID of the entity
     */
    public static void removeEntity(int entityID) {
        entities.remove(entityID);
    }

    /**
     * Checks if an entity is present in the container.
     * 
     * @param entityID
     *            The ID of the entity
     * @return Whether or not the entity is present
     */
    public static boolean isEntity(int entityID) {
        return entities.containsKey(entityID);
    }

    /**
     * Gets an entity from the container.
     * 
     * @param entityID
     *            The ID of the entity
     * @return The entity
     */
    public static FakeEntity getEntity(int entityID) {
        return entities.get(entityID);
    }

    /**
     * Gets a map of entities in the container.
     * 
     * @return The map of entities
     */
    public static DataContainer<Integer, FakeEntity> getEntityMap() {
        return entities.clone();
    }

    /**
     * Gets a collection of entities in the container.
     * 
     * @return The collection of entities
     */
    public static Collection<FakeEntity> getEntities() {
        return entities.valueSet();
    }
}