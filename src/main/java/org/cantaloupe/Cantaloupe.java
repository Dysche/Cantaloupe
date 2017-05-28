package org.cantaloupe;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.cantaloupe.command.CommandManager;
import org.cantaloupe.main.CantaloupeMain;
import org.cantaloupe.plugin.CantaloupePluginManager;
import org.cantaloupe.user.UserManager;
import org.cantaloupe.wrapper.listeners.PlayerListener;

public class Cantaloupe {
    private static CantaloupeMain          instance       = null;
    private static CantaloupePluginManager pluginManager  = null;
    private static UserManager             userManager    = null;
    private static CommandManager          commandManager = null;

    public static void initialize(CantaloupeMain plugin) {
        System.out.println("Initializing Cantaloupe.");

        // Listeners
        registerListeners();

        // Variables
        instance = plugin;

        // Plugin Manager
        pluginManager = new CantaloupePluginManager();
        pluginManager.load();

        // User Manager
        userManager = new UserManager();
        userManager.load();

        // Command Manager
        commandManager = new CommandManager();

        // Post Initalization
        new Thread() {
            @Override
            public void run() {
                while (!pluginManager.isReady()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                postInitialize();
            }
        }.start();

        System.out.println("Initialized Cantaloupe.");
    }

    private static void postInitialize() {
        System.out.println("Post-initializing Cantaloupe.");

        // User Manager
        userManager.finish();

        System.out.println("Post-initialized Cantaloupe.");
    }

    public static void deinitialize() {
        System.out.println("Deinitializing Cantaloupe.");

        // Plugin Manager
        pluginManager.unload();
        pluginManager = null;

        // User Manager
        userManager.unload();
        userManager = null;

        // Command Manager
        commandManager.unload();
        commandManager = null;

        System.out.println("Deinitialized Cantaloupe.");
    }

    private static void registerListeners() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Cantaloupe");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
    }

    public static CantaloupeMain getInstance() {
        return instance;
    }

    public static CantaloupePluginManager getPluginManager() {
        return pluginManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static UserManager getUserManager() {
        return userManager;
    }
}