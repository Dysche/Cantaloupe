package org.cantaloupe.command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.cantaloupe.plugin.CantaloupePlugin;

public class CommandManager {
	private Map<String, Map<String, CommandEntry>> commands = null;

	public CommandManager() {
		this.commands = new HashMap<String, Map<String, CommandEntry>>();
	}

	public void unload() {
		for (Map<String, CommandEntry> entries : this.commands.values()) {
			for (CommandEntry entry : entries.values()) {
				this.unregisterSpigotCommand(entry.getHandle());
			}

			entries.clear();
		}

		this.commands.clear();
		this.commands = null;
	}

	public void registerCommand(CantaloupePlugin owner, CommandSpec spec, String name, String... aliases) {
		if (!this.commands.containsKey(owner.getName())) {
			this.commands.put(owner.getName(), new HashMap<String, CommandEntry>());
		}

		CommandEntry entry = new CommandEntry(owner, spec, name, Arrays.asList(aliases));
		
		this.registerSpigotCommand(owner, entry.getHandle());
		this.commands.get(owner.getName()).put(name, entry);
	}

	public void registerCommand(CantaloupePlugin owner, CommandSpec spec, String name, ArrayList<String> aliases) {
		if (!this.commands.containsKey(owner.getName())) {
			this.commands.put(owner.getName(), new HashMap<String, CommandEntry>());
		}

		CommandEntry entry = new CommandEntry(owner, spec, name, aliases);

		this.registerSpigotCommand(owner, entry.getHandle());
		this.commands.get(owner.getName()).put(name, entry);
	}

	public void unregisterCommand(CantaloupePlugin owner, String name) {
		if (this.commands.containsKey(owner.getName())) {
			if (this.commands.get(owner.getName()).containsKey(name)) {
				this.unregisterSpigotCommand(this.commands.get(owner.getName()).get(name).getHandle());

				this.commands.get(owner.getName()).remove(name);
			}
		}
	}

	private void registerSpigotCommand(CantaloupePlugin owner, Command handle) {
		try {
			Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);

			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			commandMap.register(owner.getName() + ":" + handle.getName(), handle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unregisterSpigotCommand(Command handle) {
		try {
			Field bukkitCommandMap = Bukkit.getServer().getPluginManager().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);

			SimpleCommandMap commandMap = (SimpleCommandMap) bukkitCommandMap
					.get(Bukkit.getServer().getPluginManager());
			handle.unregister(commandMap);
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public Collection<CommandEntry> getCommands(CantaloupePlugin plugin) {
		if (this.commands.containsKey(plugin.getName())) {
			return this.commands.get(plugin).values();
		} else {
			return Collections.emptyList();
		}
	}
}