package org.cantaloupe.command;

public class CommandResult {
    public static CommandResult SUCCESS = new CommandResult();
    public static CommandResult FAILURE = new CommandResult();

    private CommandResult() {

    }
}
