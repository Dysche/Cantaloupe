package org.cantaloupe.entity;

public enum EntityType {
    // Enum Values
    ARMOR_STAND("EntityArmorStand"),
    BLAZE("EntityBlaze"),
    PLAYER("EntityPlayer");
    
    // Enum Structure
    private final String className;
    
    private EntityType(String className) {
        this.className = className;
    }
    
    public String getClassName() {
        return this.className;
    }
}