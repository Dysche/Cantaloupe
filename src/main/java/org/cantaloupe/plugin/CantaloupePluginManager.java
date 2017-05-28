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
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin instanceof CantaloupePlugin) {
                this.pluginQueue.add((CantaloupePlugin) plugin);
            }
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!isReady()) {
                    if (pluginQueue.size() > 0) {
                        for (CantaloupePlugin plugin : pluginQueue) {
                            if (plugin.isEnabled()) {
                                loadedPlugins.put(plugin.getName(), plugin);
                                pluginQueue.remove(plugin);

                                plugin.onStart();
                            }
                        }
                    } else {
                        markReady();
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();
    }

    private void markReady() {
        this.ready = true;
    }

    public void unload() {
        this.pluginQueue.clear();
        this.pluginQueue = null;

        this.loadedPlugins.forEach((string, plugin) -> plugin.onStop());
        this.loadedPlugins.clear();
        this.loadedPlugins = null;

        this.ready = false;
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