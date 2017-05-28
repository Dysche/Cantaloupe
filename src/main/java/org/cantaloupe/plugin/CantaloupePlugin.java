package org.cantaloupe.plugin;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.plugin.java.JavaPlugin;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.command.CommandEntry;
import org.cantaloupe.command.CommandSpec;

public abstract class CantaloupePlugin extends JavaPlugin {
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    public void registerCommand(CommandSpec spec, String name, String... aliases) {
        Cantaloupe.getCommandManager().registerCommand(this, spec, name, aliases);
    }

    public void registerCommand(CommandSpec spec, String name, ArrayList<String> aliases) {
        Cantaloupe.getCommandManager().registerCommand(this, spec, name, aliases);
    }

    public void unregisterCommand(String name) {
        Cantaloupe.getCommandManager().unregisterCommand(this, name);
    }

    public Collection<CommandEntry> getCommands() {
        return Cantaloupe.getCommandManager().getCommands(this);
    }

    public abstract void onStart();

    public abstract void onStop();
}