package org.cantaloupe.inventory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.nbt.NBTTagCompound;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.text.Text;
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

    public boolean hasTag() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return (boolean) ReflectionHelper.invokeMethod("hasTag", service.getItemStack(this));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ItemStack setDisplayName(Text name) {
        ItemMeta meta = this.handle.getItemMeta();
        meta.setDisplayName(name.toLegacy());

        this.handle.setItemMeta(meta);

        return this;
    }

    public ItemStack setLore(List<Text> lore) {
        List<String> loreTmp = new ArrayList<String>();

        for (Text l : lore) {
            loreTmp.add(l.toLegacy());
        }

        ItemMeta meta = this.handle.getItemMeta();
        meta.setLore(loreTmp);

        this.handle.setItemMeta(meta);

        return this;
    }

    public ItemStack setTag(NBTTagCompound tag) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object nmsStack = service.getItemStack(this);

            ReflectionHelper.invokeMethod("setTag", nmsStack, tag.toNMS());

            this.handle = (org.bukkit.inventory.ItemStack) ReflectionHelper.invokeStaticMethod("asBukkitCopy", service.BUKKIT_INVENTORY_CRAFTITEMSTACK_CLASS, service.NMS_ITEMSTACK_CLASS.cast(nmsStack));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ItemStack setItemMeta(ItemMeta meta) {
        this.handle.setItemMeta(meta);

        return this;
    }

    public ItemStack setAmount(int amount) {
        this.handle.setAmount(amount);

        return this;
    }

    public ItemStack setType(Material type) {
        this.handle.setType(type);

        return this;
    }

    public ItemStack setDurability(short durability) {
        this.handle.setDurability(durability);

        return this;
    }

    public Text getDisplayName() {
        return Text.fromLegacy(this.handle.getItemMeta().getDisplayName());
    }

    public List<Text> getLore() {
        List<Text> lore = new ArrayList<Text>();

        for (String l : this.handle.getItemMeta().getLore()) {
            lore.add(Text.fromLegacy(l));
        }

        return lore;
    }

    public NBTTagCompound getTag() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object nmsTag = ReflectionHelper.invokeMethod("getTag", service.getItemStack(this));

            if (nmsTag != null) {
                return NBTTagCompound.of(nmsTag);
            }

            return NBTTagCompound.of();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagCompound.of();
    }

    public ItemMeta getItemMeta() {
        return this.handle.getItemMeta();
    }

    public Material getType() {
        return this.handle.getType();
    }

    public int getAmount() {
        return this.handle.getAmount();
    }

    public short getDurability() {
        return this.handle.getDurability();
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