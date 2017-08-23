package org.cantaloupe.player;

public abstract class PlayerWrapper {
    private Player player = null;
    
    protected PlayerWrapper(Player player) {
        this.player = player;
    }
    
    public void onLoad() {
        
    }   
    
    public void onUnload() {
        
    }
    
    public Player getPlayer() {
        return this.player;
    }
}