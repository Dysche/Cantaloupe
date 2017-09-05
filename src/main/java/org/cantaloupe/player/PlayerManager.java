package org.cantaloupe.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.command.CommandSource;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.text.Text;

public class PlayerManager {
    private final DataContainer<UUID, Player>          players;
    private final Injector<Player>                     playerInjector;
    private final List<Class<? extends PlayerWrapper>> wrapperClasses;
    
    public PlayerManager() {
        this.players = DataContainer.of();
        this.playerInjector = Injector.of();
        this.wrapperClasses = new ArrayList<Class<? extends PlayerWrapper>>();
        
        this.registerWrapper(AudioWrapper.class);
    }

    public void inject(Scope scope, Consumer<Player> consumer) {
        this.players.valueSet().forEach(player -> player.getInjector().inject(scope, consumer));
        this.playerInjector.inject(scope, consumer);
    }

    public void injectAll(Scope scope, List<Consumer<Player>> consumers) {
        this.players.valueSet().forEach(player -> player.getInjector().injectAll(scope, consumers));
        this.playerInjector.injectAll(scope, consumers);
    }

    public void load() {
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    public void finish() {
        this.players.valueSet().forEach(player -> {
            for (Class<? extends PlayerWrapper> wrapperClass : this.wrapperClasses) {
                if (!player.hasWrapper(wrapperClass)) {
                    player.addWrapper(wrapperClass);
                }
            }

            player.onLoad();
        });
    }

    public void unload() {
        Bukkit.getOnlinePlayers().forEach(this::removePlayer);

        this.playerInjector.clear();
    }

    public void registerWrapper(Class<? extends PlayerWrapper> wrapperClazz) {
        if (this.wrapperClasses.contains(wrapperClazz)) {
            return;
        }

        this.wrapperClasses.add(wrapperClazz);
    }

    public void addPlayer(org.bukkit.entity.Player handle) {
        Player player = Player.of(handle);
        player.getInjector().injectAll(this.playerInjector);

        for (Class<? extends PlayerWrapper> wrapperClass : this.wrapperClasses) {
            player.addWrapper(wrapperClass);
        }

        this.players.put(player.getUUID(), player);
    }

    public void addPlayer(Player player) {
        player.getInjector().injectAll(this.playerInjector);

        for (Class<? extends PlayerWrapper> wrapperClass : this.wrapperClasses) {
            player.addWrapper(wrapperClass);
        }

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
        for (Player player : this.players.clone().valueSet()) {
            if (player.getName().equals(name)) {
                this.players.remove(player.getUUID());
            }
        }
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
        for (Player player : this.players.valueSet()) {
            if (player.getName().toLowerCase().startsWith(string.toLowerCase())) {
                return Optional.of(player);
            } else if (player.getName().toLowerCase().contains(string.toLowerCase())) {
                return Optional.of(player);
            }
        }

        return Optional.empty();
    }

    public <T extends PlayerWrapper> T getWrapper(Player player, Class<? extends PlayerWrapper> wrapperClass) {
        return player.getWrapper(wrapperClass);
    }

    public DataContainer<Class<? extends PlayerWrapper>, PlayerWrapper> getWrappers(Player player) {
        return player.getWrappers();
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public Collection<Player> getPlayers() {
        return this.players.valueSet();
    }

    public static class Scopes {
        public static final Scope JOIN         = Scope.of("player", "join");
        public static final Scope LEAVE        = Scope.of("player", "leave");
        public static final Scope LOAD         = Scope.of("player", "load");
        public static final Scope UNLOAD       = Scope.of("player", "unload");
        public static final Scope FIRST_JOIN   = Scope.of("player", "first_join");

        public static final Scope WORLD_SWITCH = Scope.of("player", "world_switch");
    }
}