package org.cantaloupe.model;

import org.bukkit.Material;
import org.cantaloupe.inventory.ItemStack;

public class Model {
    private final String   name;
    private final Material material;
    private final short    damage;

    private Model(String name, Material material, short damage) {
        this.name = name;
        this.material = material;
        this.damage = damage;
    }

    public static Model of(String name, Material material, short damage) {
        return new Model(name, material, damage);
    }

    public ItemStack getStack() {
        return ItemStack.of(this.material).setDurability(this.damage);
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public short getDamage() {
        return this.damage;
    }
}