package org.cantaloupe.permission;

/**
 * An interface containing basic permission holder methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IPermissionHolder {
    /**
     * Checks if the holder has a permission node.
     * 
     * @param node
     *            The node
     * @return True if it does, false if not
     */
    public boolean hasPermission(String node);
}