package org.cantaloupe.main;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.plugin.CantaloupePlugin;

public class CantaloupeMain extends CantaloupePlugin {
    @Override
    public void onEnable() {
        Cantaloupe.initialize(this);
    }

    @Override
    public void onDisable() {
        Cantaloupe.deinitialize();
    }

    @Override
    public void onPreInit() {
        
    }

    @Override
    public void onInit() {
        
    }

    @Override
    public void onDeinit() {
        
    }
}