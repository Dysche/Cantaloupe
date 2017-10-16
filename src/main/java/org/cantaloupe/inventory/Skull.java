package org.cantaloupe.inventory;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.cantaloupe.nbt.NBTTagCompound;
import org.cantaloupe.nbt.NBTTagList;
import org.cantaloupe.player.Player;
import org.cantaloupe.skin.Skin;

/**
 * A class used to create a skull.
 * 
 * @author Dylan Scheltens
 *
 */
public class Skull {
    private ItemStack handle = null;

    private Skull(SkullType type) {
        this.handle = ItemStack.of(Material.SKULL_ITEM, (byte) type.ordinal());
    }

    private Skull(Skin skin) {
        this.handle = ItemStack.of(Material.SKULL_ITEM, (byte) 3);
        this.injectTextureNBT(skin.getTexture());
    }

    /**
     * Creates and returns a new skull from a player.
     * 
     * @param player
     *            The player
     * @return The skull
     */
    public static Skull of(Player player) {
        return new Skull(Skin.of(player));
    }

    /**
     * Creates and returns a new skull from a UUID.
     * 
     * @param uuid
     *            The uuid
     * @return The skull
     */
    public static Skull of(UUID uuid) {
        return new Skull(Skin.of(uuid));
    }

    /**
     * Creates and returns a new skull from a player name.
     * 
     * @param name
     *            The name
     * @return The skull
     */
    public static Skull of(String name) {
        return new Skull(Skin.of(name));
    }

    private void injectTextureNBT(String texture) {
        NBTTagCompound tag = this.handle.getTag();

        NBTTagCompound skullOwner = tag.getCompound("SkullOwner");
        skullOwner.setString("Id", UUID.randomUUID().toString());

        NBTTagCompound properties = skullOwner.getCompound("Properties");
        NBTTagList textures = NBTTagList.of();

        NBTTagCompound value = NBTTagCompound.of();
        value.setString("Value", texture);

        textures.add(value);
        properties.set("textures", textures);
        skullOwner.set("Properties", properties);
        tag.set("SkullOwner", skullOwner);

        this.handle.setTag(tag);
    }

    /**
     * Returns the handle of the skull.
     * 
     * @return The handle
     */
    public ItemStack toHandle() {
        return this.handle;
    }
}