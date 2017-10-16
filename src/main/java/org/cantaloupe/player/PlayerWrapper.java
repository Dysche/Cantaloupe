package org.cantaloupe.player;

/**
 * An abstract class containing methods for player wrappers.
 * 
 * @author Dylan Scheltens
 *
 */
public abstract class PlayerWrapper {
    private Player player = null;

    public PlayerWrapper(Player player) {
        this.player = player;
    }

    /**
     * This is called once the wrapper gets loaded.
     */
    protected void onLoad() {

    }

    /**
     * This is called once the wrapper gets post-loaded.
     */
    protected void onPostLoad() {

    }
    
    /**
     * This is called once the wrapper gets unloaded.
     */
    protected void onUnload() {

    }

    /**
     * This is called once the player joins.
     */
    protected void onJoin() {
        
    }

    /**
     * This is called once the player leaves.
     */
    protected void onLeave() {
        
    }

    /**
     * Gets the owner of the wrapper.
     * 
     * @return The owner
     */
    public Player getPlayer() {
        return this.player;
    }
}