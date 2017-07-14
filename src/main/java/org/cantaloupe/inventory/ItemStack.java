package org.cantaloupe.inventory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.nbt.NBTTagCompound;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;

public class ItemStack {
    private org.bukkit.inventory.ItemStack handle;

    private ItemStack(org.bukkit.inventory.ItemStack handle) {
        this.handle = handle;
    }

    public static ItemStack of(Material material) {
        return new ItemStack(new org.bukkit.inventory.ItemStack(material));
    }

    public static ItemStack of(Material material, byte data) {
        return new ItemStack(new org.bukkit.inventory.ItemStack(material, 1, data));
    }

    public static ItemStack of(Material material, int amount) {
        return new ItemStack(new org.bukkit.inventory.ItemStack(material, amount));
    }

    public static ItemStack of(Material material, int amount, byte data) {
        return new ItemStack(new org.bukkit.inventory.ItemStack(material, amount, data));
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

    public void setItemMeta(ItemMeta meta) {
        this.handle.setItemMeta(meta);
    }

    public ItemMeta getItemMeta() {
        return this.handle.getItemMeta();
    }

    public void setTag(NBTTagCompound tag) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object nmsStack = service.getItemStack(this);

            ReflectionHelper.invokeMethod("setTag", nmsStack, tag.toNMS());
            
            this.handle = (org.bukkit.inventory.ItemStack) ReflectionHelper.invokeStaticMethod("asBukkitCopy", service.BUKKIT_INVENTORY_CRAFTITEMSTACK_CLASS, service.NMS_ITEMSTACK_CLASS.cast(nmsStack));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public NBTTagCompound getTag() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object nmsTag = ReflectionHelper.invokeMethod("getTag", service.getItemStack(this));
            
            if(nmsTag != null) {
                return NBTTagCompound.of(nmsTag);
            }
            
            return NBTTagCompound.of();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagCompound.of();
    }

    public boolean hasTag() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return (boolean) ReflectionHelper.invokeMethod("hasTag", service.getItemStack(this));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    public org.bukkit.inventory.ItemStack toHandle() {
        return this.handle;
    }

    public Object toNMS() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        return service.getItemStack(this);
    }

    @Override
    public boolean equals(Object other) {
        return this.handle.equals(other);
    }
}