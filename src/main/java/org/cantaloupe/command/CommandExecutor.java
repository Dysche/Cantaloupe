package org.cantaloupe.command;

import org.cantaloupe.command.args.CommandContext;

public interface CommandExecutor {
	CommandResult execute(CommandSource src, CommandContext args);
}