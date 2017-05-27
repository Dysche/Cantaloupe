package org.cantaloupe.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.text.Text;
import org.cantaloupe.user.UserManager.Scopes;
import org.cantaloupe.user.permission.IPermissionHolder;
import org.cantaloupe.user.permission.IPermittable;
import org.cantaloupe.user.permission.group.Group;
import org.cantaloupe.user.permission.group.GroupManager;

public class User implements IPermittable, IPermissionHolder {
	private Player handle = null;
	private Injector<User> injector = null;
	private PermissionAttachment permissionAttachment = null;
	private ArrayList<Group> groups = null;

	private User(Player handle) {
		this.handle = handle;
		this.injector = new Injector<User>();

		this.permissionAttachment = this.handle.addAttachment(Cantaloupe.getInstance());
		this.groups = new ArrayList<Group>();
	}

	public static User of(Player handle) {
		return new User(handle);
	}

	public void onJoin() {
		Optional<List<Consumer<User>>> consumers = this.getInjector().getConsumers(Scopes.JOIN);
		if (consumers.isPresent()) {
			for (Consumer<User> consumer : consumers.get()) {
				consumer.accept(this);
			}
		}
	}

	public void onLoad() {
		Optional<List<Consumer<User>>> consumers = this.getInjector().getConsumers(Scopes.LOAD);
		if (consumers.isPresent()) {
			for (Consumer<User> consumer : consumers.get()) {
				consumer.accept(this);
			}
		}
	}

	public void onLeave() {
		Optional<List<Consumer<User>>> consumers = this.getInjector().getConsumers(Scopes.LEAVE);
		if (consumers.isPresent()) {
			for (Consumer<User> consumer : consumers.get()) {
				consumer.accept(this);
			}
		}
	}

	public void onUnload() {
		Optional<List<Consumer<User>>> consumers = this.getInjector().getConsumers(Scopes.UNLOAD);
		if (consumers.isPresent()) {
			for (Consumer<User> consumer : consumers.get()) {
				consumer.accept(this);
			}
		}

		this.getInjector().clear();
	}

	public void sendMessage(Text text) {
		this.handle.spigot().sendMessage(text.getComponent());
	}

	public void sendMessage(String message) {
		this.handle.sendMessage(message);
	}

	public void sendLegacyMessage(String message) {
		this.handle.spigot().sendMessage(Text.fromLegacy(message).getComponent());
	}

	public boolean isInGroup(String name) {
		for (Group group : this.groups) {
			if (group.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public boolean isInGroup(Group group) {
		return this.groups.contains(group);
	}

	public void joinGroup(String name) {
		if (this.isInGroup(name)) {
			return;
		}
		
		Group group = GroupManager.getGroup(name);
		for (String node : group.getPermissions()) {
			this.permissionAttachment.setPermission(node, true);
		}
		
		this.groups.add(group);
	}

	public void joinGroup(Group group) {
		for (String node : group.getPermissions()) {
			this.permissionAttachment.setPermission(node, true);
		}
		
		this.groups.add(group);
	}

	public void leaveGroup(String name) {
		Group group = GroupManager.getGroup(name);

		for (String node : group.getPermissions()) {
			boolean hasPermission = false;

			for (Group g : this.groups) {
				if (g.hasPermission(node)) {
					hasPermission = true;
				}
			}

			if (!hasPermission) {
				this.permissionAttachment.setPermission(node, true);
			}
		}

		this.groups.remove(group);
	}

	public void leaveGroup(Group group) {
		for (String node : group.getPermissions()) {
			boolean hasPermission = false;

			for (Group g : this.groups) {
				if (g.hasPermission(node)) {
					hasPermission = true;
				}
			}

			if (!hasPermission) {
				this.permissionAttachment.setPermission(node, true);
			}
		}

		this.groups.remove(group);
	}

	@Override
	public void grantPermission(String node) {
		this.permissionAttachment.setPermission(node, true);
	}

	@Override
	public void revokePermission(String node) {
		this.permissionAttachment.setPermission(node, false);
	}

	@Override
	public boolean hasPermission(String node) {
		boolean hasPermission = this.handle.hasPermission(node);

		if (!hasPermission) {
			for (Group group : this.groups) {
				if (group.hasPermission(node)) {
					return true;
				}
			}

			return false;
		} else {
			return hasPermission;
		}
	}

	public Player toHandle() {
		return this.handle;
	}

	public UUID getUUID() {
		return this.handle.getUniqueId();
	}

	public String getName() {
		return this.handle.getName();
	}

	public Injector<User> getInjector() {
		return this.injector;
	}

	public Collection<Group> getGroups() {
		return this.groups;
	}
}