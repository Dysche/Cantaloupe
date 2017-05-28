package org.cantaloupe.permission;

import org.bukkit.World;

public interface IPermittable {
    public void setPermission(String node);

    public void setPermission(World world, String node);

    public void unsetPermission(String node);

    public void unsetPermission(World world, String node);
}