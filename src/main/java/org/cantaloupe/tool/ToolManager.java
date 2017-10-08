package org.cantaloupe.tool;

import java.util.Collection;
import java.util.Optional;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.inventory.ItemStack;

public class ToolManager {
    private final DataContainer<String, Tool> tools;

    private ToolManager() {
        this.tools = DataContainer.of();
    }

    public static ToolManager of() {
        return new ToolManager();
    }

    public void addTool(Tool tool) {
        this.tools.put(tool.getID(), tool);
    }

    public void removeTool(Tool tool) {
        this.removeTool(tool.getID());
    }

    public void removeTool(String name) {
        this.tools.remove(name);
    }

    public boolean hasTool(String name) {
        return this.tools.containsKey(name);
    }

    public Optional<Tool> getTool(org.bukkit.inventory.ItemStack itemStackHandle) {
        ItemStack itemStack = ItemStack.of(itemStackHandle);

        if (itemStack.hasTag() && itemStack.getTag().getBoolean("isTool")) {
            return Optional.ofNullable(this.getTool(itemStack.getTag().getString("toolID")));
        }

        return Optional.empty();
    }

    public Optional<Tool> getTool(ItemStack itemStack) {
        if (itemStack.hasTag() && itemStack.getTag().getBoolean("isTool")) {
            return Optional.ofNullable(this.getTool(itemStack.getTag().getString("toolID")));
        }

        return Optional.empty();
    }

    public Tool getTool(String name) {
        return this.tools.get(name);
    }

    public Collection<Tool> getTools() {
        return this.tools.valueSet();
    }
}