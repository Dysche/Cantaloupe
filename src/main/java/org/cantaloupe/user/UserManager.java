package org.cantaloupe.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cantaloupe.command.CommandSource;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.text.Text;

public class UserManager {
	private Map<UUID, User> users = null;
	private Injector<User> userInjector = null;

	public UserManager() {
		this.users = new HashMap<UUID, User>();
		this.userInjector = new Injector<User>();
	}

	public void inject(Scope scope, Consumer<User> consumer) {
		this.users.values().forEach(user -> user.getInjector().inject(scope, consumer));
		this.userInjector.inject(scope, consumer);
	}

	public void load() {
		Bukkit.getOnlinePlayers().forEach(this::addUser);
	}

	public void finish() {
		for (User user : this.users.values()) {
			user.onLoad();
		}
	}

	public void unload() {
		Bukkit.getOnlinePlayers().forEach(this::removeUser);

		this.userInjector.clear();
	}

	public void addUser(Player player) {
		User user = User.of(player);
		user.getInjector().injectAll(this.userInjector);

		this.users.put(user.getUUID(), user);
	}

	public void addUser(User user) {
		user.getInjector().injectAll(this.userInjector);

		this.users.put(user.getUUID(), user);
	}

	public void removeUser(User user) {
		this.users.remove(user.getUUID());
	}

	public void removeUser(Player player) {
		Optional<User> user = this.getUser(player.getUniqueId());
		if (user.isPresent()) {
			user.get().onUnload();
		}

		this.users.remove(player.getUniqueId());
	}

	public void removeUser(UUID uuid) {
		Optional<User> user = this.getUser(uuid);
		if (user.isPresent()) {
			user.get().onUnload();
		}

		this.users.remove(uuid);
	}

	public void removeUser(String name) {
		Map<UUID, User> clone = new HashMap<UUID, User>();
		clone.putAll(this.users);

		for (User user : clone.values()) {
			if (user.getName().equals(name)) {
				this.users.remove(user.getUUID());
			}
		}
	}

	public int userCount() {
		return this.users.size();
	}

	public void broadcast(Text text) {
		this.users.forEach((uuid, user) -> {
			user.sendMessage(text);
		});
	}

	public void broadcast(String message) {
		this.users.forEach((uuid, user) -> {
			user.sendMessage(message);
		});
	}

	public void broadcastLegacy(String message) {
		this.users.forEach((uuid, user) -> {
			user.sendLegacyMessage(message);
		});
	}

	public Optional<User> getUserFromCommandSource(CommandSource source) {
		return this.tryGetUser(source.getName());
	}
	
	public Optional<User> getUserFromCommandSender(CommandSender sender) {
		return this.tryGetUser(sender.getName());
	}

	public Optional<User> getUserFromHandle(Player player) {
		return Optional.of(this.users.get(player.getUniqueId()));
	}

	public Optional<User> getUser(UUID uuid) {
		return Optional.of(this.users.get(uuid));
	}

	public Optional<User> tryGetUser(String string) {
		for (User user : this.users.values()) {
			if (user.getName().toLowerCase().startsWith(string.toLowerCase())) {
				return Optional.of(user);
			} else if (user.getName().toLowerCase().contains(string.toLowerCase())) {
				return Optional.of(user);
			}
		}

		return Optional.empty();
	}

	public Collection<User> getUsers() {
		return this.users.values();
	}

	public static class Scopes {
		public static final Scope JOIN = Scope.of("user", "join");
		public static final Scope LEAVE = Scope.of("user", "leave");
		public static final Scope LOAD = Scope.of("user", "load");
		public static final Scope UNLOAD = Scope.of("user", "unload");
	}
}