package org.cantaloupe.entity;

import java.util.Collection;

import org.cantaloupe.data.DataContainer;

public class FakeEntityContainer {
    private static DataContainer<Integer, FakeEntity> entities = DataContainer.of();

    public static void addEntity(FakeEntity entity) {
        entities.put(entity.getEntityID(), entity);
    }

    public static void removeEntity(FakeEntity entity) {
        removeEntity(entity.getEntityID());
    }

    public static void removeEntity(int entityID) {
        entities.remove(entityID);
    }

    public static boolean isEntity(int entityID) {
        return entities.containsKey(entityID);
    }

    public static DataContainer<Integer, FakeEntity> getEntityMap() {
        return entities.clone();
    }

    public static Collection<FakeEntity> getEntities() {
        return entities.valueSet();
    }

    public static FakeEntity getEntity(int entityID) {
        return entities.get(entityID);
    }
}