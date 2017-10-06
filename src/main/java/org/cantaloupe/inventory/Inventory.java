package org.cantaloupe.inventory;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;

public class Inventory implements IInventory<org.bukkit.inventory.Inventory> {
    private final org.bukkit.inventory.Inventory handle;

    protected Inventory(Player owner, int size, Text name) {
        this.handle = Bukkit.createInventory(owner.toHandle(), size, name.toLegacy());
    }

    protected Inventory(Player owner, InventoryType type, Text name) {
        this.handle = Bukkit.createInventory(owner.toHandle(), type, name.toLegacy());
    }

    protected Inventory(org.bukkit.inventory.Inventory handle) {
        this.handle = handle;
    }

    public static Inventory of(Player owner, int size, Text name) {
        return new Inventory(owner, size, name);
    }

    public static Inventory of(Player owner, InventoryType type, Text name) {
        return new Inventory(owner, type, name);
    }

    public static Inventory of(Player owner, int size) {
        return new Inventory(owner, size, Text.of("Inventory"));
    }

    public static Inventory of(Player owner, InventoryType type) {
        return new Inventory(owner, type, Text.of("Inventory"));
    }

    public static Inventory of(Player owner, Text name) {
        return new Inventory(owner, 54, name);
    }

    public static Inventory of(Player owner) {
        return new Inventory(owner, 54, Text.of("Inventory"));
    }

    public static Inventory of(org.bukkit.inventory.Inventory handle) {
        return new Inventory(handle);
    }

    @Override
    public org.bukkit.inventory.Inventory toHandle() {
        return this.handle;
    }
}