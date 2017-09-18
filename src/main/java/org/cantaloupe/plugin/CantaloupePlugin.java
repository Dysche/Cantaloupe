package org.cantaloupe.plugin;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.plugin.java.JavaPlugin;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.command.CommandEntry;
import org.cantaloupe.command.CommandSpec;

/**
 * A class containing the methods for a cantaloupe plugin.
 * 
 * @author Dylan Scheltens
 *
 */
public abstract class CantaloupePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        this.onInit();
    }

    @Override
    public void onDisable() {
        this.onDeinit();
    }

    /**
     * Registers a command to the server.
     * 
     * @param spec
     *            The command's spec
     * @param name
     *            The command's name
     * @param aliases
     *            An array of aliases
     */
    public void registerCommand(CommandSpec spec, String name, String... aliases) {
        Cantaloupe.getCommandManager().registerCommand(this, spec, name, aliases);
    }

    /**
     * Registers a command to the server.
     * 
     * @param spec
     *            The command's spec
     * @param name
     *            The command's name
     * @param aliases
     *            A list of aliases
     */
    public void registerCommand(CommandSpec spec, String name, ArrayList<String> aliases) {
        Cantaloupe.getCommandManager().registerCommand(this, spec, name, aliases);
    }

    /**
     * Unregisters a command from the server.
     * 
     * @param name
     *            The command's name
     */
    public void unregisterCommand(String name) {
        Cantaloupe.getCommandManager().unregisterCommand(this, name);
    }

    /**
     * Returns a collection of registered commands for the plugin.
     * 
     * @return The collection of plugins
     */
    public Collection<CommandEntry> getCommands() {
        return Cantaloupe.getCommandManager().getCommands(this);
    }

    /**
     * Gets called on pre-initialization.
     */
    public abstract void onPreInit();

    /**
     * Gets called on initialization.
     */
    public abstract void onInit();

    /**
     * Gets called on post-initialization.
     */
    public void onPostInit() {}

    /**
     * Gets called on de-initialization.
     */
    public abstract void onDeinit();

    /**
     * Gets the ID of the plugin.
     * 
     * @return The ID
     */
    public String getID() {
        return this.getName();
    }
}