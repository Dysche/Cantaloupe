package org.cantaloupe;

import java.io.IOException;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.cantaloupe.audio.AudioServer;
import org.cantaloupe.command.CommandManager;
import org.cantaloupe.commands.CMDAudioServer;
import org.cantaloupe.main.CantaloupeMain;
import org.cantaloupe.player.PlayerManager;
import org.cantaloupe.plugin.CantaloupePlugin;
import org.cantaloupe.plugin.CantaloupePluginManager;
import org.cantaloupe.service.ServiceManager;
import org.cantaloupe.util.CantaloupeClassLoader;
import org.cantaloupe.world.WorldManager;
import org.cantaloupe.wrapper.listeners.PickupListenerNew;
import org.cantaloupe.wrapper.listeners.PickupListenerOld;
import org.cantaloupe.wrapper.listeners.PlayerListener;
import org.cantaloupe.wrapper.listeners.WorldListener;

public class Cantaloupe {
    private static CantaloupeMain          instance       = null;
    private static CantaloupePluginManager pluginManager  = null;
    private static PlayerManager           playerManager  = null;
    private static WorldManager            worldManager   = null;
    private static CommandManager          commandManager = null;
    private static ServiceManager          serviceManager = null;
    private static AudioServer             audioServer    = null;

    public static void initialize(CantaloupeMain plugin) {
        System.out.println("Initializing Cantaloupe.");

        // Variables
        instance = plugin;

        // Internal
        registerLibraries();
        registerListeners();

        // Service Manager
        serviceManager = ServiceManager.of();
        serviceManager.load();

        // Player Manager
        playerManager = PlayerManager.of();
        playerManager.load();

        // World Manager
        worldManager = WorldManager.of();
        worldManager.load();

        // Command Manager
        commandManager = CommandManager.of();
        registerCommands();

        // Plugin Manager
        pluginManager = CantaloupePluginManager.of();
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

    /**
     * Sets the audio server up for use.
     * 
     * @param port
     *            The port to listen to
     * @return The audio server
     */
    public static AudioServer setupAudioServer(int port) {
        if (audioServer == null) {
            audioServer = AudioServer.of(port);
        }

        return audioServer;
    }

    /**
     * Starts the audio server.
     */
    public static void startAudioServer() {
        audioServer.start();
    }

    /**
     * Stops the audio server.
     */
    public static void stopAudioServer() {
        audioServer.stop();
    }

    private static void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), getInstance());
        Bukkit.getPluginManager().registerEvents(new WorldListener(), getInstance());

        try {
            Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");

            Bukkit.getPluginManager().registerEvents(new PickupListenerNew(), getInstance());
        } catch (ClassNotFoundException e) {
            Bukkit.getPluginManager().registerEvents(new PickupListenerOld(), getInstance());
        }
    }

    private static void registerLibraries() {
        try {
            CantaloupeClassLoader.addFile("lib/joml-1.9.3.jar");
            CantaloupeClassLoader.addFile("lib/bson-3.3.0.jar");
            CantaloupeClassLoader.addFile("lib/mongodb-driver-3.3.0.jar");
            CantaloupeClassLoader.addFile("lib/mongodb-driver-core-3.3.0.jar");
            CantaloupeClassLoader.addFile("lib/java-websocket-1.3.0.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void registerCommands() {
        commandManager.registerCommand(instance, CMDAudioServer.create(), "audio");
    }

    /**
     * Registers an event listener.
     * 
     * @param listener
     *            The listener to register
     */
    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getInstance());
    }

    /**
     * Registers an event listener.
     * 
     * @param listener
     *            The listener to register
     * 
     * @param plugin
     *            The plugin
     */
    public static void registerListener(Listener listener, CantaloupePlugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Checks if th audio server has been setup/
     * 
     * @return True if it has, false if not
     */
    public static boolean isAudioServerSetup() {
        return audioServer != null;
    }

    /**
     * Gets the plugin instance.
     * 
     * @return The plugin instance
     */
    public static CantaloupeMain getInstance() {
        return instance;
    }

    /**
     * Gets the {@link org.cantaloupe.plugin.CantaloupePlugin plugin} manager.
     * 
     * @return The world manager
     */
    public static CantaloupePluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Gets the {@link org.cantaloupe.player.Player player} manager.
     * 
     * @return The world manager
     */
    public static PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * Gets the {@link org.cantaloupe.world.World world} manager.
     * 
     * @return The world manager
     */
    public static WorldManager getWorldManager() {
        return worldManager;
    }

    /**
     * Gets the {@link org.cantaloupe.command.CommandSpec command} manager.
     * 
     * @return The command manager
     */
    public static CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Gets the {@link org.cantaloupe.service.IService service} manager.
     * 
     * @return The service manager
     */
    public static ServiceManager getServiceManager() {
        return serviceManager;
    }

    /**
     * Gets the audio server.
     * 
     * @return An optional containing the audio server if it's been setup, an
     *         empty optional if not
     */
    public static Optional<AudioServer> getAudioServer() {
        return Optional.ofNullable(audioServer);
    }
}