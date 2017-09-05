package org.cantaloupe.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.cantaloupe.command.CommandSpec.ErrorType;
import org.cantaloupe.command.args.ArgumentParseException;
import org.cantaloupe.command.args.CommandArgs;
import org.cantaloupe.command.args.CommandContext;
import org.cantaloupe.command.args.parsing.SingleArg;
import org.cantaloupe.text.Text;

public class CommandChild {
    private CommandSpec  owner   = null;
    private CommandSpec  spec    = null;
    private String       name    = null;
    private List<String> aliases = null;

    protected CommandChild(CommandSpec owner, CommandSpec spec, String name, List<String> aliases) {
        this.owner = owner;
        this.spec = spec;
        this.name = name;
        this.aliases = aliases;
    }

    protected boolean execute(CommandSource source, String[] arguments) {
        if (this.spec.getPermission() != null) {
            if (!source.hasPermission(this.spec.getPermission())) {
                if (this.spec.getErrors().containsKey(ErrorType.NO_PERMS)) {
                    source.sendMessage(this.spec.getErrors().get(ErrorType.NO_PERMS));
                } else {
                    source.sendMessage("You do not have the required permissions to execute that command.");
                }

                return true;
            }
        }

        if (arguments.length == 0) {
            CommandContext context = this.processArguments(source, arguments);
            if (context == null) {
                return true;
            }

            return this.spec.getExecutor().execute(source, context) == CommandResult.SUCCESS;
        } else if (arguments.length >= 1) {
            CommandChild child = getChild(arguments[0]);

            if (child != null) {
                return child.execute(source, Arrays.copyOfRange(arguments, 1, arguments.length));
            } else {
                CommandContext context = this.processArguments(source, arguments);
                if (context == null) {
                    return true;
                }

                return this.spec.getExecutor().execute(source, context) == CommandResult.SUCCESS;
            }
        }

        return false;
    }

    private CommandContext processArguments(CommandSource source, String[] args) {
        CommandContext context = new CommandContext();
        ArrayList<SingleArg> arguments = new ArrayList<SingleArg>();

        for (int i = 0; i < args.length; i++) {
            arguments.add(new SingleArg(args[i], i, i + 1));
        }

        try {
            spec.getArguments().parse(source, new CommandArgs(this.spec, String.join(" ", args), arguments), context);
        } catch (ArgumentParseException e) {
            source.sendMessage(e.getText());
        }

        return context;
    }

    private CommandChild getChild(String argument) {
        for (CommandChild child : this.spec.getChildren()) {
            if (child.getName().equals(argument) || child.getAliases().contains(argument)) {
                return child;
            }
        }

        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.spec.getPermission();
    }

    public Optional<Text> getUsage() {
        return this.spec.getUsage();
    }

    public Optional<Text> getHelp() {
        return this.spec.getHelp();
    }

    public Optional<Text> getDescription() {
        return this.spec.getDescription();
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public CommandSpec getOwner() {
        return this.owner;
    }
}