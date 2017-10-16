package org.cantaloupe.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.command.CommandSource;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.inject.IInjectable;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.text.Text;

/**
 * A class used to manage players.
 * 
 * @author Dylan Scheltens
 *
 */
public class PlayerManager implements IInjectable<Player> {
    private final DataContainer<UUID, Player>          players;
    private final Injector<Player>                     playerInjector;
    private final List<Class<? extends PlayerWrapper>> wrapperClasses;

    private PlayerManager() {
        this.players = DataContainer.of();
        this.playerInjector = Injector.of();
        this.wrapperClasses = new ArrayList<Class<? extends PlayerWrapper>>();

        this.registerWrapper(AudioWrapper.class);
    }

    /**
     * Creates and returns a new player manager.
     * 
     * @return The player manager
     */
    public static PlayerManager of() {
        return new PlayerManager();
    }

    @Override
    public void inject(Scope scope, Consumer<Player> consumer) {
        this.players.valueSet().forEach(player -> player.getInjector().inject(scope, consumer));
        this.playerInjector.inject(scope, consumer);
    }

    @Override
    public void injectAll(Scope scope, List<Consumer<Player>> consumers) {
        this.players.valueSet().forEach(player -> player.getInjector().injectAll(scope, consumers));
        this.playerInjector.injectAll(scope, consumers);
    }

    /**
     * Loads the player manager.
     */
    public void load() {
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    /**
     * Finishes loading the player manager.
     */
    public void finish() {
        this.players.valueSet().forEach(player -> {
            for (Class<? extends PlayerWrapper> wrapperClass : this.wrapperClasses) {
                if (!player.hasWrapper(wrapperClass)) {
                    player.addWrapper(wrapperClass);
                }
            }

            player.onLoad();
        });

        this.players.valueSet().forEach(player -> {
            player.onPostLoad();
        });
    }

    /**
     * Unloads the player manager.
     */
    public void unload() {
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);

        this.playerInjector.clear();
    }

    /**
     * Registers a wrapper to the player manager.
     * 
     * @param wrapperClazz
     *            The type of the wrapper
     */
    public void registerWrapper(Class<? extends PlayerWrapper> wrapperClazz) {
        if (this.wrapperClasses.contains(wrapperClazz)) {
            return;
        }

        this.wrapperClasses.add(wrapperClazz);
    }

    /**
     * Adds a player to the player manager.
     * 
     * @param handle
     *            The handle of the player
     */
    public void addPlayer(org.bukkit.entity.Player handle) {
        Player player = Player.of(handle);
        player.getInjector().injectAll(this.playerInjector);

        for (Class<? extends PlayerWrapper> wrapperClass : this.wrapperClasses) {
            player.addWrapper(wrapperClass);
        }

        this.players.put(player.getUUID(), player);
    }

    /**
     * Adds a player to the player manager.
     * 
     * @param player
     *            The player
     */
    public void addPlayer(Player player) {
        player.getInjector().injectAll(this.playerInjector);

        for (Class<? extends PlayerWrapper> wrapperClass : this.wrapperClasses) {
            player.addWrapper(wrapperClass);
        }

        this.players.put(player.getUUID(), player);
    }

    /**
     * Removes a player from the player manager.
     * 
     * @param handle
     *            The handle of the player
     */
    public void removePlayer(org.bukkit.entity.Player handle) {
        Optional<Player> player = this.getPlayerFromHandle(handle);
        if (player.isPresent()) {
            player.get().onUnload();
        }

        this.players.remove(handle.getUniqueId());
    }

    /**
     * Removes a player from the player manager.
     * 
     * @param player
     *            The player
     */
    public void removePlayer(Player player) {
        this.players.remove(player.getUUID());
    }

    /**
     * Removes a player from the player manager.
     * 
     * @param uuid
     *            The uuid of the player
     */
    public void removePlayer(UUID uuid) {
        Optional<Player> player = this.getPlayer(uuid);
        if (player.isPresent()) {
            player.get().onUnload();
        }

        this.players.remove(uuid);
    }

    /**
     * Removes a player from the player manager.
     * 
     * @param name
     *            The name of the player
     */
    public void tryRemovePlayer(String name) {
        for (Player player : this.players.clone().valueSet()) {
            if (player.getName().equals(name)) {
                this.players.remove(player.getUUID());
            }
        }
    }

    /**
     * Broadcasts a message to all players.
     * 
     * @param message
     *            The message
     */
    public void broadcast(Text message) {
        this.players.forEach((uuid, player) -> {
            player.sendMessage(message);
        });
    }

    /**
     * Broadcasts a message to all players.
     * 
     * @param message
     *            The message
     */
    public void broadcast(String message) {
        this.players.forEach((uuid, player) -> {
            player.sendMessage(message);
        });
    }

    /**
     * Broadcasts a formatted message to all players.
     * 
     * @param message
     *            The message
     */
    public void broadcastLegacy(String message) {
        this.players.forEach((uuid, player) -> {
            player.sendLegacyMessage(message);
        });
    }

    /**
     * Plays a sound to all players.
     * 
     * @param sound
     *            The sound
     */
    public void playSound(Sound sound) {
        this.players.forEach((uuid, player) -> {
            player.playSound(sound);
        });
    }

    /**
     * Plays a sound to all players.
     * 
     * @param sound
     *            The sound
     * 
     * @param volume
     *            The volume of the sound
     */
    public void playSound(Sound sound, float volume) {
        this.players.forEach((uuid, player) -> {
            player.playSound(sound, volume);
        });
    }

    /**
     * Plays a sound to all players.
     * 
     * @param sound
     *            The sound
     * 
     * @param volume
     *            The volume of the sound
     * 
     * @param pitch
     *            The pitch of the sound
     */
    public void playSound(Sound sound, float volume, float pitch) {
        this.players.forEach((uuid, player) -> {
            player.playSound(sound, volume, pitch);
        });
    }

    /**
     * Gets a player from a command source.
     * 
     * @param source
     *            The command source
     * @return An optional containing the player if it's present, an empty
     *         optional if not
     */
    public Optional<Player> getPlayerFromCommandSource(CommandSource source) {
        return this.tryGetPlayer(source.getName());
    }

    /**
     * Gets a player from a command sender.
     * 
     * @param sender
     *            The command sender
     * @return An optional containing the player if it's present, an empty
     *         optional if not
     */
    public Optional<Player> getPlayerFromCommandSender(CommandSender sender) {
        return this.tryGetPlayer(sender.getName());
    }

    /**
     * Gets a player by handle.
     * 
     * @param handle
     *            The player handle
     * @return An optional containing the player if it's present, an empty
     *         optional if not
     */
    public Optional<Player> getPlayerFromHandle(org.bukkit.entity.Player handle) {
        return Optional.ofNullable(this.players.get(handle.getUniqueId()));
    }

    /**
     * Gets a player from the player manager.
     * 
     * @param uuid
     *            The UUID of the player
     * @return An optional containing the player if it's present, an empty
     *         optional if not
     */
    public Optional<Player> getPlayer(UUID uuid) {
        return Optional.ofNullable(this.players.get(uuid));
    }

    /**
     * Gets a player by name.
     * 
     * @param name
     *            The name of the player
     * @return An optional containing the player if it's present, an empty
     *         optional if not
     */
    public Optional<Player> tryGetPlayer(String name) {
        for (Player player : this.players.valueSet()) {
            if (player.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return Optional.of(player);
            } else if (player.getName().toLowerCase().contains(name.toLowerCase())) {
                return Optional.of(player);
            }
        }

        return Optional.empty();
    }

    /**
     * Gets a wrapper from a player.
     * 
     * @param <T>
     *            The type of the wrapper
     * @param player
     *            The player
     * @param wrapperClass
     *            The type of the wrapper
     * @return An optional containing the wrapper if it's present, an empty
     *         optional if not
     */
    public <T extends PlayerWrapper> T getWrapper(Player player, Class<T> wrapperClass) {
        return player.getWrapper(wrapperClass);
    }

    /**
     * Gets a list of wrappers from a player.
     * 
     * @param player
     *            The player
     * @return The list of wrappers
     */
    public Collection<PlayerWrapper> getWrappers(Player player) {
        return player.getWrappers();
    }

    /**
     * Gets the amount of players.
     * 
     * @return The amount of players
     */
    public int getPlayerCount() {
        return this.players.size();
    }

    /**
     * Gets a collection of players from the player manager.
     * 
     * @return The collection of players
     */
    public Collection<Player> getPlayers() {
        return this.players.valueSet();
    }

    @Override
    public Injector<Player> getInjector() {
        return this.playerInjector;
    }

    /**
     * A class containing the possible player-related scopes.
     * 
     * @author Dylan Scheltens
     *
     */
    public static class Scopes {
        public static final Scope JOIN         = Scope.of("player", "join");
        public static final Scope LEAVE        = Scope.of("player", "leave");
        public static final Scope LOAD         = Scope.of("player", "load");
        public static final Scope POST_LOAD    = Scope.of("player", "post_load");
        public static final Scope UNLOAD       = Scope.of("player", "unload");
        public static final Scope FIRST_JOIN   = Scope.of("player", "first_join");

        public static final Scope WORLD_SWITCH = Scope.of("player", "world_switch");
    }
}