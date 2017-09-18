package org.cantaloupe.entity;

/**
 * An enum containing the possible types of entity.
 * 
 * @author Dylan Scheltens
 *
 */
public enum EntityType {
    // Enum Values
    ARMOR_STAND("EntityArmorStand"), BLAZE("EntityBlaze"), PLAYER("EntityPlayer");

    // Enum Structure
    private final String className;

    private EntityType(String className) {
        this.className = className;
    }

    /**
     * Gets the name of the class.
     * 
     * @return The class name
     */
    public String getClassName() {
        return this.className;
    }
}