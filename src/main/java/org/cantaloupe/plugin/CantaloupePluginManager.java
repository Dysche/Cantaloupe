package org.cantaloupe.plugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class CantaloupePluginManager {
    private Queue<CantaloupePlugin>       pluginQueue   = null;
    private Map<String, CantaloupePlugin> loadedPlugins = null;
    private boolean                       ready         = false;

    public CantaloupePluginManager() {
        this.pluginQueue = new LinkedList<CantaloupePlugin>();
        this.loadedPlugins = new HashMap<String, CantaloupePlugin>();
    }

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
    
    public void finish() {
        this.loadedPlugins.forEach((ID, plugin) -> plugin.onPostInit());
    }

    public void unload() {
        this.pluginQueue.clear();
        this.pluginQueue = null;

        this.loadedPlugins.clear();
        this.loadedPlugins = null;

        this.ready = false;
    }
    
    private void markReady() {
        this.ready = true;
    }

    public boolean isReady() {
        return this.ready;
    }

    public boolean isPluginLoaded(String ID) {
        return this.loadedPlugins.containsKey(ID);
    }

    public Map<String, CantaloupePlugin> getPlugins() {
        return this.loadedPlugins;
    }
}