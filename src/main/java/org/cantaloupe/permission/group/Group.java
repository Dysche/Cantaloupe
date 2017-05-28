package org.cantaloupe.permission.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.cantaloupe.permission.IPermissionHolder;
import org.cantaloupe.permission.IPermittable;
import org.cantaloupe.text.Text;
import org.cantaloupe.user.User;

public abstract class Group implements IPermittable, IPermissionHolder {
    private Map<String, List<String>> permissions = new HashMap<String, List<String>>();

    {
        this.permissions = new HashMap<String, List<String>>();
        this.permissions.put("_global_", new ArrayList<String>());
    }

    public abstract void initialize();

    public boolean hasPermission(User user, String node) {
        for (String world : this.permissions.keySet()) {
            if (user.toHandle().getWorld().getName().equals(world)) {
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
        if (this.getPermissions().containsKey(world.getName())) {
            this.permissions.get(world.getName()).remove(node);
        }
    }

    @Override
    public void unsetPermission(String node) {
        this.permissions.get("_global_").remove(node);
    }

    public boolean isDefault() {
        return false;
    }

    public abstract String getName();

    public abstract Text getPrefix();

    public abstract Text getDescription();

    public Map<String, List<String>> getPermissions() {
        return this.permissions;
    }

    public List<String> getPermissions(World world) {
        return world != null != this.permissions.containsKey(world.getName()) ? this.permissions.get(world.getName()) : this.permissions.get("_global_");
    }
}