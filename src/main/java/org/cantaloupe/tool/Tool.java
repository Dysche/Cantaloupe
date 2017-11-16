package org.cantaloupe.tool;

import org.bukkit.block.Block;
import org.cantaloupe.inventory.ItemStack;
import org.cantaloupe.nbt.NBTTagCompound;
import org.cantaloupe.player.Player;
import org.cantaloupe.world.WorldObject;

public abstract class Tool {
    private final String    ID;
    private final ItemStack itemStack;

    protected Tool(String ID, ItemStack itemStack) {
        this.ID = ID;
        this.itemStack = itemStack;

        this.setup();
    }

    private void setup() {
        if (this.itemStack.hasTag()) {
            this.itemStack.setTag(this.itemStack.getTag().setBoolean("isTool", true).setString("toolID", this.ID));
        } else {
            this.itemStack.setTag(NBTTagCompound.of().setBoolean("isTool", true).setString("toolID", this.ID));
        }
    }

    public void onLeftClick(Player player, Block block) {

    }

    public void onRightClick(Player player, Block block) {

    }

    public void onLeftClickObject(Player player, WorldObject object) {

    }

    public void onRightClickObject(Player player, WorldObject object) {

    }

    public String getID() {
        return this.ID;
    }

    public ItemStack getStack() {
        return this.itemStack;
    }
}