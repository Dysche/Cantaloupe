package org.cantaloupe.inventory;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.meta.SkullMeta;
import org.cantaloupe.nbt.NBTTagCompound;
import org.cantaloupe.nbt.NBTTagList;
import org.cantaloupe.player.Player;

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

    private Skull(Object texture) {
        this.handle = ItemStack.of(Material.SKULL_ITEM, (byte) 3);
        this.injectNBT((String) texture);
    }

    private Skull(UUID uuid) {
        this.handle = ItemStack.of(Material.SKULL_ITEM, (byte) 3);

        SkullMeta meta = (SkullMeta) this.handle.getItemMeta();
        meta.setOwner(uuid.toString());

        this.handle.setItemMeta(meta);
    }

    private Skull(String name) {
        this.handle = ItemStack.of(Material.SKULL_ITEM, (byte) 3);

        SkullMeta meta = (SkullMeta) this.handle.getItemMeta();
        meta.setOwner(name);

        this.handle.setItemMeta(meta);
    }

    /**
     * Creates and returns a new skull from a player.
     * 
     * @param player
     *            The player
     * @return The skull
     */
    public static Skull of(Player player) {
        return new Skull(player.getUUID());
    }

    /**
     * Creates and returns a new skull from a player UUID.
     * 
     * @param uuid
     *            The uuid
     * @return The skull
     */
    public static Skull of(UUID uuid) {
        return new Skull(uuid);
    }

    /**
     * Creates and returns a new skull from a player name.
     * 
     * @param name
     *            The name
     * @return The skull
     */
    public static Skull of(String name) {
        return new Skull(name);
    }

    /**
     * Creates and returns a new skull from a texture.
     * 
     * @param texture
     *            The texture
     * @return The skull
     */
    public static Skull fromTexture(String texture) {
        return new Skull((Object) texture);
    }

    private void injectNBT(String texture) {
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