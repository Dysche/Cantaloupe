package org.cantaloupe.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.cantaloupe.command.CommandSource;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.text.Text;

public class PlayerManager {
    private Map<UUID, Player> players        = null;
    private Injector<Player>  playerInjector = null;

    public PlayerManager() {
        this.players = new HashMap<UUID, Player>();
        this.playerInjector = new Injector<Player>();
    }

    public void inject(Scope scope, Consumer<Player> consumer) {
        this.players.values().forEach(player -> player.getInjector().inject(scope, consumer));
        this.playerInjector.inject(scope, consumer);
    }

    public void load() {
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    public void finish() {
        this.players.values().forEach(player -> player.onLoad());
    }

    public void unload() {
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);

        this.playerInjector.clear();
    }

    public void addPlayer(org.bukkit.entity.Player handle) {
        Player player = Player.of(handle);
        player.getInjector().injectAll(this.playerInjector);

        this.players.put(player.getUUID(), player);
    }

    public void addPlayer(Player player) {
        player.getInjector().injectAll(this.playerInjector);

        this.players.put(player.getUUID(), player);
    }

    public void removePlayer(org.bukkit.entity.Player handle) {
        Optional<Player> player = this.getPlayerFromHandle(handle);
        if (player.isPresent()) {
            player.get().onUnload();
        }

        this.players.remove(handle.getUniqueId());
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getUUID());
    }

    public void removePlayer(UUID uuid) {
        Optional<Player> player = this.getPlayer(uuid);
        if (player.isPresent()) {
            player.get().onUnload();
        }

        this.players.remove(uuid);
    }

    public void removePlayer(String name) {
        Map<UUID, Player> clone = new HashMap<UUID, Player>();
        clone.putAll(this.players);

        for (Player player : clone.values()) {
            if (player.getName().equals(name)) {
                this.players.remove(player.getUUID());
            }
        }
    }

    public int playerCount() {
        return this.players.size();
    }

    public void broadcast(Text text) {
        this.players.forEach((uuid, player) -> {
            player.sendMessage(text);
        });
    }

    public void broadcast(String message) {
        this.players.forEach((uuid, player) -> {
            player.sendMessage(message);
        });
    }

    public void broadcastLegacy(String message) {
        this.players.forEach((uuid, player) -> {
            player.sendLegacyMessage(message);
        });
    }

    public Optional<Player> getPlayerFromCommandSource(CommandSource source) {
        return this.tryGetPlayer(source.getName());
    }

    public Optional<Player> getPlayerFromCommandSender(CommandSender sender) {
        return this.tryGetPlayer(sender.getName());
    }

    public Optional<Player> getPlayerFromHandle(org.bukkit.entity.Player player) {
        return Optional.of(this.players.get(player.getUniqueId()));
    }

    public Optional<Player> getPlayer(UUID uuid) {
        return Optional.of(this.players.get(uuid));
    }

    public Optional<Player> tryGetPlayer(String string) {
        for (Player player : this.players.values()) {
            if (player.getName().toLowerCase().startsWith(string.toLowerCase())) {
                return Optional.of(player);
            } else if (player.getName().toLowerCase().contains(string.toLowerCase())) {
                return Optional.of(player);
            }
        }

        return Optional.empty();
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    public static class Scopes {
        public static final Scope JOIN         = Scope.of("player", "join");
        public static final Scope LEAVE        = Scope.of("player", "leave");
        public static final Scope LOAD         = Scope.of("player", "load");
        public static final Scope UNLOAD       = Scope.of("player", "unload");

        public static final Scope WORLD_SWITCH = Scope.of("player", "world_switch");
    }
}