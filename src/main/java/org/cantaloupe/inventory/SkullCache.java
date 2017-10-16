package org.cantaloupe.inventory;

import java.util.UUID;

import org.cantaloupe.data.DataContainer;

public class SkullCache {
    private static DataContainer<UUID, Skull>   uuidSkulls = DataContainer.of();
    private static DataContainer<String, Skull> nameSkulls = DataContainer.of();

    public static void addSkull(UUID uuid, Skull skull) {
        uuidSkulls.put(uuid, skull);
    }

    public static void addSkull(String name, Skull skull) {
        nameSkulls.put(name, skull);
    }

    public static void removeSkull(UUID uuid) {
        uuidSkulls.remove(uuid);
    }

    public static void removeSkull(String name) {
        nameSkulls.remove(name);
    }

    public static boolean hasSkull(UUID uuid) {
        return uuidSkulls.containsKey(uuid);
    }

    public static boolean hasSkull(String name) {
        return nameSkulls.containsKey(name);
    }

    public static Skull getSkull(UUID uuid) {
        return uuidSkulls.get(uuid);
    }

    public static Skull getSkull(String name) {
        return nameSkulls.get(name);
    }
}
