package org.cantaloupe.player;

/**
 * An abstract class containing methods for player wrappers.
 * 
 * @author Dylan Scheltens
 *
 */
public abstract class PlayerWrapper {
    private Player player = null;

    protected PlayerWrapper(Player player) {
        this.player = player;
    }

    /**
     * This is called once the wrapper gets loaded.
     */
    public void onLoad() {

    }

    /**
     * This is called once the wrapper gets unloaded.
     */
    public void onUnload() {

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