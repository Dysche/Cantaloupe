package org.cantaloupe.wrapper.listeners;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.user.User;
import org.cantaloupe.user.permission.group.GroupManager;

public class PlayerListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		User user = User.of(event.getPlayer());
		Cantaloupe.getUserManager().addUser(user);
		user.onJoin();
		user.onLoad();

		event.setJoinMessage(null);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Optional<User> userOpt = Cantaloupe.getUserManager().getUserFromHandle(event.getPlayer());

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.onUnload();
			user.onLeave();

			Cantaloupe.getUserManager().removeUser(user);
		}

		event.setQuitMessage(null);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Optional<User> userOpt = Cantaloupe.getUserManager().getUserFromHandle(event.getPlayer());
		if (userOpt.isPresent()) {
			User user = userOpt.get();

			// Format
			event.setFormat("<" + GroupManager.getPrefixFor(user).toLegacy() + user.getName() + "> "
					+ event.getMessage());
		}
	}
}