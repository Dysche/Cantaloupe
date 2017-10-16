package org.cantaloupe.skin;

import org.cantaloupe.data.DataContainer;

public class SkinCache {
    private static DataContainer<String, Skin> skins = DataContainer.of();

    public static void addSkin(String ID, Skin skin) {
        skins.put(ID, skin);
    }

    public static void removeSkin(String ID) {
        skins.remove(ID);
    }

    public static boolean hasSkin(String ID) {
        return skins.containsKey(ID);
    }

    public static Skin getSkin(String ID) {
        return skins.get(ID);
    }
}