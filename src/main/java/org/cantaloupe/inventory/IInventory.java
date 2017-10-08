package org.cantaloupe.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.player.Player;
import org.cantaloupe.player.PlayerManager;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.location.ImmutableLocation;

public interface IInventory<T extends org.bukkit.inventory.Inventory> {
    public default IInventory<T> clear() {
        this.toHandle().clear();

        return this;
    }

    public default IInventory<T> clear(int index) {
        this.toHandle().clear(index);

        return this;
    }

    public default boolean contains(ItemStack itemStack) {
        return this.toHandle().contains(itemStack.toHandle());
    }

    public default boolean contains(ItemStack itemStack, int amount) {
        return this.toHandle().contains(itemStack.toHandle(), amount);
    }

    public default boolean contains(Material material) {
        return this.toHandle().contains(material);
    }

    public default boolean contains(Material material, int amount) {
        return this.toHandle().contains(material, amount);
    }

    public default boolean containsAtLeast(ItemStack itemStack, int amount) {
        return this.toHandle().containsAtLeast(itemStack.toHandle(), amount);
    }

    public default DataContainer<Integer, ItemStack> all(ItemStack itemStack) {
        DataContainer<Integer, ItemStack> itemStacks = DataContainer.of();

        this.toHandle().all(itemStack.toHandle()).forEach((index, itemStackHandle) -> {
            itemStacks.put(index, ItemStack.of(itemStackHandle));
        });

        return itemStacks;
    }

    public default DataContainer<Integer, ItemStack> all(Material material) {
        DataContainer<Integer, ItemStack> itemStacks = DataContainer.of();

        this.toHandle().all(material).forEach((index, itemStackHandle) -> {
            itemStacks.put(index, ItemStack.of(itemStackHandle));
        });

        return itemStacks;
    }

    public default int first(ItemStack itemStack) {
        return this.toHandle().first(itemStack.toHandle());
    }

    public default int first(Material material) {
        return this.toHandle().first(material);
    }

    public default int firstEmpty() {
        return this.toHandle().firstEmpty();
    }

    public default IInventory<T> addItem(ItemStack itemStack) {
        this.toHandle().addItem(itemStack.toHandle());

        return this;
    }

    public default IInventory<T> addItems(ItemStack... itemStacks) {
        return this.addItems(Arrays.asList(itemStacks));
    }

    public default IInventory<T> addItems(List<ItemStack> itemStacks) {
        return this.addItems((Collection<ItemStack>) itemStacks);
    }

    public default IInventory<T> addItems(Collection<ItemStack> itemStacks) {
        List<org.bukkit.inventory.ItemStack> itemHandles = new ArrayList<org.bukkit.inventory.ItemStack>();

        for (ItemStack stack : itemStacks) {
            itemHandles.add(stack.toHandle());
        }

        this.toHandle().addItem(itemHandles.toArray(new org.bukkit.inventory.ItemStack[0]));

        return this;
    }

    public default IInventory<T> remove(ItemStack itemStack) {
        this.toHandle().remove(itemStack.toHandle());

        return this;
    }

    public default IInventory<T> remove(Material material) {
        this.toHandle().remove(material);

        return this;
    }

    public default IInventory<T> removeItem(ItemStack item) {
        this.toHandle().removeItem(item.toHandle());

        return this;
    }

    public default IInventory<T> removeItems(ItemStack... itemStacks) {
        return this.removeItems(Arrays.asList(itemStacks));
    }

    public default IInventory<T> removeItems(List<ItemStack> itemStacks) {
        return this.removeItems((Collection<ItemStack>) itemStacks);
    }

    public default IInventory<T> removeItems(Collection<ItemStack> itemStacks) {
        List<org.bukkit.inventory.ItemStack> itemHandles = new ArrayList<org.bukkit.inventory.ItemStack>();

        for (ItemStack stack : itemStacks) {
            itemHandles.add(stack.toHandle());
        }

        this.toHandle().removeItem(itemHandles.toArray(new org.bukkit.inventory.ItemStack[0]));

        return this;
    }

    public default IInventory<T> setItem(int index, ItemStack itemStack) {
        this.toHandle().setItem(index, itemStack.toHandle());

        return this;
    }

    public default IInventory<T> setContents(ItemStack... itemStacks) {
        return this.setContents(Arrays.asList(itemStacks));
    }

    public default IInventory<T> setContents(List<ItemStack> itemStacks) {
        return this.setContents((Collection<ItemStack>) itemStacks);
    }

    public default IInventory<T> setContents(Collection<ItemStack> itemStacks) {
        List<org.bukkit.inventory.ItemStack> itemHandles = new ArrayList<org.bukkit.inventory.ItemStack>();

        for (ItemStack stack : itemStacks) {
            itemHandles.add(stack.toHandle());
        }

        this.toHandle().setContents(itemHandles.toArray(new org.bukkit.inventory.ItemStack[0]));

        return this;
    }

    public default IInventory<T> setStorageContents(ItemStack... itemStacks) {
        return this.setStorageContents(Arrays.asList(itemStacks));
    }

    public default IInventory<T> setStorageContents(List<ItemStack> itemStacks) {
        return this.setStorageContents((Collection<ItemStack>) itemStacks);
    }

    public default IInventory<T> setStorageContents(Collection<ItemStack> itemStacks) {
        List<org.bukkit.inventory.ItemStack> itemHandles = new ArrayList<org.bukkit.inventory.ItemStack>();

        for (ItemStack stack : itemStacks) {
            itemHandles.add(stack.toHandle());
        }

        this.toHandle().setStorageContents(itemHandles.toArray(new org.bukkit.inventory.ItemStack[0]));

        return this;
    }

    public default IInventory<T> setMaxStackSize(int maxStackSize) {
        this.toHandle().setMaxStackSize(maxStackSize);

        return this;
    }

    public default Text getName() {
        return Text.fromLegacy(this.toHandle().getName());
    }

    public default int getSize() {
        return this.toHandle().getSize();
    }

    public default InventoryType getType() {
        return this.toHandle().getType();
    }

    public default Text getTitle() {
        return Text.fromLegacy(this.toHandle().getTitle());
    }

    public default int getMaxStackSize() {
        return this.toHandle().getMaxStackSize();
    }

    public default ImmutableLocation getLocation() {
        return ImmutableLocation.of(this.toHandle().getLocation());
    }

    public default ItemStack getItem(int index) {
        return ItemStack.of(this.toHandle().getItem(index));
    }

    public default List<ItemStack> getContents() {
        org.bukkit.inventory.ItemStack[] itemHandles = this.toHandle().getContents();
        ItemStack[] items = new ItemStack[itemHandles.length];

        for (int i = 0; i < itemHandles.length; i++) {
            items[i] = ItemStack.of(itemHandles[i]);
        }

        return Arrays.asList(items);
    }

    public default List<ItemStack> getStorageContents() {
        org.bukkit.inventory.ItemStack[] itemHandles = this.toHandle().getStorageContents();
        ItemStack[] items = new ItemStack[itemHandles.length];

        for (int i = 0; i < itemHandles.length; i++) {
            items[i] = ItemStack.of(itemHandles[i]);
        }

        return Arrays.asList(items);
    }

    public default List<Player> getViewers() {
        List<HumanEntity> playerHandles = this.toHandle().getViewers();
        List<Player> players = new ArrayList<Player>();
        PlayerManager playerManager = Cantaloupe.getPlayerManager();

        for (HumanEntity playerHandle : playerHandles) {
            players.add(playerManager.getPlayerFromHandle((org.bukkit.entity.Player) playerHandle).get());
        }

        return players;
    }

    public default InventoryHolder getHolder() {
        return this.toHandle().getHolder();
    }

    public T toHandle();
}