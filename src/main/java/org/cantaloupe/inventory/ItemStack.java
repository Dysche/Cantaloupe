package org.cantaloupe.inventory;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStack {
    private final org.bukkit.inventory.ItemStack handle;

    private ItemStack(org.bukkit.inventory.ItemStack handle) {
        this.handle = handle;
    }

    public static ItemStack of(Material material) {
        return new ItemStack(new org.bukkit.inventory.ItemStack(material));
    }

    public static ItemStack of(Material material, int amount) {
        return new ItemStack(new org.bukkit.inventory.ItemStack(material, amount));
    }

    public static ItemStack of(org.bukkit.inventory.ItemStack handle) {
        return new ItemStack(handle);
    }

    public void setDisplayName(String name) {
        ItemMeta meta = this.handle.getItemMeta();
        meta.setDisplayName(name);

        this.handle.setItemMeta(meta);
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = this.handle.getItemMeta();
        meta.setLore(lore);

        this.handle.setItemMeta(meta);
    }

    public org.bukkit.inventory.ItemStack toHandle() {
        return this.handle;
    }

    @Override
    public boolean equals(Object other) {
        return this.handle.equals(other);
    }
}