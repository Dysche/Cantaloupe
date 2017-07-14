package org.cantaloupe;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.cantaloupe.command.CommandManager;
import org.cantaloupe.main.CantaloupeMain;
import org.cantaloupe.player.PlayerManager;
import org.cantaloupe.plugin.CantaloupePluginManager;
import org.cantaloupe.service.ServiceManager;
import org.cantaloupe.util.CantaloupeClassLoader;
import org.cantaloupe.world.WorldManager;
import org.cantaloupe.wrapper.listeners.PlayerListener;

public class Cantaloupe {
    private static CantaloupeMain          instance       = null;
    private static CantaloupePluginManager pluginManager  = null;
    private static PlayerManager           playerManager  = null;
    private static WorldManager            worldManager   = null;
    private static CommandManager          commandManager = null;
    private static ServiceManager          serviceManager = null;

    public static void initialize(CantaloupeMain plugin) {
        System.out.println("Initializing Cantaloupe.");

        // Internal
        registerLibraries();
        registerListeners();

        // Variables
        instance = plugin;

        // Service Manager
        serviceManager = new ServiceManager();
        serviceManager.load();
        
        // Player Manager
        playerManager = new PlayerManager();
        playerManager.load();

        // World Manager
        worldManager = new WorldManager();
        worldManager.load();

        // Command Manager
        commandManager = new CommandManager();

        // Plugin Manager
        pluginManager = new CantaloupePluginManager();
        pluginManager.load();

        System.out.println("Initialized Cantaloupe.");
        
        // Post Initialization
        postInitialize();
    }

    private static void postInitialize() {
        System.out.println("Post-initializing Cantaloupe.");

        // Player Manager
        playerManager.finish();

        // Plugin Manager
        pluginManager.finish();

        System.out.println("Post-initialized Cantaloupe.");
    }

    public static void deinitialize() {
        System.out.println("Deinitializing Cantaloupe.");

        // Plugin Manager
        pluginManager.unload();
        pluginManager = null;

        // World Manager
        worldManager.unload();
        worldManager = null;
        
        // Player Manager
        playerManager.unload();
        playerManager = null;

        // Command Manager
        commandManager.unload();
        commandManager = null;
        
        // Service Manager
        serviceManager.unload();
        serviceManager = null;

        System.out.println("Deinitialized Cantaloupe.");
    }

    private static void registerListeners() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Cantaloupe");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
    }

    private static void registerLibraries() {
        try {
            CantaloupeClassLoader.addFile("libs/joml-1.9.3.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CantaloupeMain getInstance() {
        return instance;
    }

    public static CantaloupePluginManager getPluginManager() {
        return pluginManager;
    }

    public static PlayerManager getPlayerManager() {
        return playerManager;
    }

    public static WorldManager getWorldManager() {
        return worldManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static ServiceManager getServiceManager() {
        return serviceManager;
    }
}