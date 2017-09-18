package org.cantaloupe.command;

import org.cantaloupe.command.args.CommandContext;

public interface ICommandExecutor {
    CommandResult execute(CommandSource src, CommandContext args);
}