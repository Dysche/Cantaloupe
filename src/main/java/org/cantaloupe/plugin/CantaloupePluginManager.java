package org.cantaloupe.plugin;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.cantaloupe.data.DataContainer;

/**
 * A class used to manage plugins.
 * 
 * @author Dylan Scheltens
 *
 */
public class CantaloupePluginManager {
    private final Queue<CantaloupePlugin>                 pluginQueue;
    private final DataContainer<String, CantaloupePlugin> loadedPlugins;
    private boolean                                       ready = false;

    private CantaloupePluginManager() {
        this.pluginQueue = new LinkedList<CantaloupePlugin>();
        this.loadedPlugins = DataContainer.of();
    }

    /**
     * Creates and returns a new plugin manager.
     * 
     * @return The plugin manager
     */
    public static CantaloupePluginManager of() {
        return new CantaloupePluginManager();
    }

    /**
     * Loads the plugin manager.
     */
    public void load() {
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (p instanceof CantaloupePlugin) {
                CantaloupePlugin plugin = (CantaloupePlugin) p;

                loadedPlugins.put(plugin.getName(), plugin);
                plugin.onPreInit();
            }
        }

        markReady();
    }

    /**
     * Finishes loading the plugin manager.
     */
    public void finish() {
        this.loadedPlugins.forEach((ID, plugin) -> plugin.onPostInit());
    }

    /**
     * Unloads the plugin manager.
     */
    public void unload() {
        this.pluginQueue.clear();
        this.loadedPlugins.clear();

        this.ready = false;
    }

    private void markReady() {
        this.ready = true;
    }

    /**
     * Checks if the plugin manager is ready.
     * 
     * @return True if it is, false if not
     */
    public boolean isReady() {
        return this.ready;
    }

    /**
     * Checks if a plugin is loaded.
     * 
     * @param ID
     *            The ID of a plugin
     * @return True if it is, false if not
     */
    public boolean isPluginLoaded(String ID) {
        return this.loadedPlugins.containsKey(ID);
    }

    /**
     * Gets a plugin from the plugin manager.
     * 
     * @param ID
     *            The ID of a plugin
     * @return An optional containing the plugin if it's present, an empty
     *         optional if not
     */
    public Optional<CantaloupePlugin> getPlugin(String ID) {
        return Optional.ofNullable(this.loadedPlugins.get(ID));
    }

    /**
     * Gets a collection of plugins from the manager.
     * 
     * @return The collection of plugins
     */
    public Collection<CantaloupePlugin> getPlugins() {
        return this.loadedPlugins.valueSet();
    }
}