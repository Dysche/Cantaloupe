package org.cantaloupe.permission.group;

import java.util.ArrayList;
import java.util.List;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.permission.IPermissionHolder;
import org.cantaloupe.permission.IPermittable;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.World;

/**
 * A class containing the methods for a group.
 * 
 * @author Dylan Scheltens
 *
 */
public abstract class Group implements IPermittable, IPermissionHolder {
    private DataContainer<String, List<String>> permissions;

    {
        this.permissions = DataContainer.of();
        this.permissions.put("_global_", new ArrayList<String>());
    }

    /**
     * This is called on initialization of the group.
     */
    public abstract void initialize();

    /**
     * Checks if a player has a permission node.
     * 
     * @param player
     *            The player
     * @param node
     *            The node
     * @return True if it does, false if not
     */
    public boolean hasPermission(Player player, String node) {
        for (String world : this.permissions.keySet()) {
            if (player.toHandle().getWorld().getName().equals(world)) {
                if (this.permissions.get(world).contains(node)) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return this.permissions.get("_global_").contains(node);
    }

    @Override
    public boolean hasPermission(String node) {
        return this.permissions.get("_global_").contains(node);
    }

    @Override
    public void setPermission(World world, String node) {
        if (!this.permissions.containsKey(world.getName())) {
            this.permissions.put(world.getName(), new ArrayList<String>());
        }

        this.permissions.get(world.getName()).add(node);
    }

    @Override
    public void setPermission(String node) {
        this.permissions.get("_global_").add(node);
    }

    @Override
    public void unsetPermission(World world, String node) {
        if (this.permissions.containsKey(world.getName())) {
            this.permissions.get(world.getName()).remove(node);
        }
    }

    @Override
    public void unsetPermission(String node) {
        this.permissions.get("_global_").remove(node);
    }

    /**
     * Checks if the group is the default group.
     * 
     * @return True if it is, false if not
     */
    public boolean isDefault() {
        return false;
    }

    /**
     * Checks if the prefix of the group should be shown.
     * 
     * @return True if it should, false if not
     */
    public abstract boolean showPrefix();

    /**
     * Gets the name of the group.
     * 
     * @return The name
     */
    public abstract String getName();

    /**
     * Gets the prefix of the group.
     * 
     * @return The prefix
     */
    public abstract Text getPrefix();

    /**
     * Gets the description of the group.
     * 
     * @return The description
     */
    public abstract Text getDescription();

    /**
     * Gets the entries from the group with the world name and list of
     * permissions.
     * 
     * @return The map
     */
    public DataContainer<String, List<String>> getPermissionMap() {
        return this.permissions.clone();
    }

    /**
     * Gets a list of permission from the group for a world.
     * 
     * @param world
     *            The world
     * @return The list of permissions
     */
    public List<String> getPermissions(World world) {
        return world != null && this.permissions.containsKey(world.getName()) ? this.permissions.get(world.getName()) : this.permissions.get("_global_");
    }
}