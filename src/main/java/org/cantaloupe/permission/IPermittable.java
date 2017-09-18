package org.cantaloupe.permission;

import org.cantaloupe.world.World;

/**
 * An interface containing basic permission methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IPermittable {
    /**
     * Sets a permission node.
     * 
     * @param node
     *            The node
     */
    public void setPermission(String node);

    /**
     * Sets a permission node for a world.
     * 
     * @param world
     *            The world
     * @param node
     *            The node
     */
    public void setPermission(World world, String node);

    /**
     * Unsets a permission node.
     * 
     * @param node
     *            The node
     */
    public void unsetPermission(String node);

    /**
     * Unsets a permission node for a world.
     * 
     * @param world
     *            The world
     * @param node
     *            The node
     */
    public void unsetPermission(World world, String node);
}