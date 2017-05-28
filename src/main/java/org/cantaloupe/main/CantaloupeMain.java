package org.cantaloupe.main;

import org.bukkit.plugin.java.JavaPlugin;
import org.cantaloupe.Cantaloupe;

public class CantaloupeMain extends JavaPlugin {
    @Override
    public void onEnable() {
        Cantaloupe.initialize(this);
    }

    @Override
    public void onDisable() {
        Cantaloupe.deinitialize();
    }
}