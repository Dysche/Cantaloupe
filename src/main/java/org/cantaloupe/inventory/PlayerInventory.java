package org.cantaloupe.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cantaloupe.tool.Tool;

public class PlayerInventory implements IInventory<org.bukkit.inventory.PlayerInventory> {
    private final org.bukkit.inventory.PlayerInventory handle;
    private boolean                                    locked = false;

    private PlayerInventory(org.bukkit.inventory.PlayerInventory handle) {
        this.handle = handle;
    }

    public static PlayerInventory of(org.bukkit.inventory.PlayerInventory handle) {
        return new PlayerInventory(handle);
    }

    public static PlayerInventory of(org.bukkit.inventory.PlayerInventory handle, boolean locked) {
        return new PlayerInventory(handle).setLocked(locked);
    }

    public PlayerInventory addTool(Tool tool) {
        this.addItem(tool.getStack());

        return this;
    }

    public PlayerInventory setTool(int index, Tool tool) {
        this.setItem(index, tool.getStack());

        return this;
    }

    public PlayerInventory setItemInHand(EnumItemSlot slot, ItemStack itemStack) {
        if (slot == EnumItemSlot.MAINHAND) {
            return this.setItemInMainHand(itemStack);
        } else if (slot == EnumItemSlot.OFFHAND) {
            return this.setItemInOffHand(itemStack);
        }

        return this;
    }

    public PlayerInventory setItemInMainHand(ItemStack itemStack) {
        this.handle.setItemInMainHand(itemStack != null ? itemStack.toHandle() : null);

        return this;
    }

    public PlayerInventory setItemInOffHand(ItemStack itemStack) {
        this.handle.setItemInOffHand(itemStack != null ? itemStack.toHandle() : null);

        return this;
    }

    public PlayerInventory setHeldItemSlot(int slot) {
        this.handle.setHeldItemSlot(slot);

        return this;
    }

    public PlayerInventory setExtraContents(ItemStack... itemStacks) {
        return this.setExtraContents(Arrays.asList(itemStacks));
    }

    public PlayerInventory setExtraContents(List<ItemStack> itemStacks) {
        return this.setExtraContents((Collection<ItemStack>) itemStacks);
    }

    public PlayerInventory setExtraContents(Collection<ItemStack> itemStacks) {
        List<org.bukkit.inventory.ItemStack> itemHandles = new ArrayList<org.bukkit.inventory.ItemStack>();

        for (ItemStack itemStack : itemStacks) {
            itemHandles.add(itemStack != null ? itemStack.toHandle() : null);
        }

        this.handle.setExtraContents(itemHandles.toArray(new org.bukkit.inventory.ItemStack[0]));

        return this;
    }

    public PlayerInventory setArmorContents(ItemStack... itemStacks) {
        return this.setArmorContents(Arrays.asList(itemStacks));
    }

    public PlayerInventory setArmorContents(List<ItemStack> itemStacks) {
        return this.setArmorContents((Collection<ItemStack>) itemStacks);
    }

    public PlayerInventory setArmorContents(Collection<ItemStack> itemStacks) {
        List<org.bukkit.inventory.ItemStack> itemHandles = new ArrayList<org.bukkit.inventory.ItemStack>();

        for (ItemStack itemStack : itemStacks) {
            itemHandles.add(itemStack != null ? itemStack.toHandle() : null);
        }

        this.handle.setStorageContents(itemHandles.toArray(new org.bukkit.inventory.ItemStack[0]));

        return this;
    }

    public PlayerInventory setHelmet(ItemStack itemStack) {
        this.handle.setHelmet(itemStack != null ? itemStack.toHandle() : null);

        return this;
    }

    public PlayerInventory setChestplate(ItemStack itemStack) {
        this.handle.setChestplate(itemStack != null ? itemStack.toHandle() : null);

        return this;
    }

    public PlayerInventory setLeggings(ItemStack itemStack) {
        this.handle.setLeggings(itemStack != null ? itemStack.toHandle() : null);

        return this;
    }

    public PlayerInventory setBoots(ItemStack itemStack) {
        this.handle.setBoots(itemStack != null ? itemStack.toHandle() : null);

        return this;
    }

    public PlayerInventory setLocked(boolean locked) {
        this.locked = locked;

        return this;
    }

    public boolean isTool(ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().getBoolean("isTool");
    }

    public boolean isLocked() {
        return this.locked;
    }

    public ItemStack getItemInHand(EnumItemSlot slot) {
        if (slot == EnumItemSlot.MAINHAND) {
            return this.getItemInMainHand();
        } else if (slot == EnumItemSlot.OFFHAND) {
            return this.getItemInOffHand();
        }

        return null;
    }

    public ItemStack getItemInMainHand() {
        return this.handle.getItemInMainHand() != null ? ItemStack.of(this.handle.getItemInMainHand()) : null;
    }

    public ItemStack getItemInOffHand() {
        return this.handle.getItemInOffHand() != null ? ItemStack.of(this.handle.getItemInOffHand()) : null;
    }

    public int getHeldItemSlot() {
        return this.handle.getHeldItemSlot();
    }

    public List<ItemStack> getExtraContents() {
        org.bukkit.inventory.ItemStack[] itemHandles = this.toHandle().getExtraContents();
        ItemStack[] items = new ItemStack[itemHandles.length];

        for (int i = 0; i < itemHandles.length; i++) {
            items[i] = itemHandles[i] != null ? ItemStack.of(itemHandles[i]) : null;
        }

        return Arrays.asList(items);
    }

    public List<ItemStack> getArmorContents() {
        org.bukkit.inventory.ItemStack[] itemHandles = this.toHandle().getArmorContents();
        ItemStack[] items = new ItemStack[itemHandles.length];

        for (int i = 0; i < itemHandles.length; i++) {
            items[i] = itemHandles[i] != null ? ItemStack.of(itemHandles[i]) : null;
        }

        return Arrays.asList(items);
    }

    public ItemStack getBoots() {
        return this.handle.getBoots() != null ? ItemStack.of(this.handle.getBoots()) : null;
    }

    public ItemStack getLeggings() {
        return this.handle.getLeggings() != null ? ItemStack.of(this.handle.getLeggings()) : null;
    }

    public ItemStack getChestplate() {
        return this.handle.getChestplate() != null ? ItemStack.of(this.handle.getChestplate()) : null;
    }

    public ItemStack getHelmet() {
        return this.handle.getHelmet() != null ? ItemStack.of(this.handle.getHelmet()) : null;
    }

    @Override
    public org.bukkit.inventory.PlayerInventory toHandle() {
        return this.handle;
    }
}