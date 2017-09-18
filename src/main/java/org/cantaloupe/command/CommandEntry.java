package org.cantaloupe.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.cantaloupe.command.CommandSpec.ErrorType;
import org.cantaloupe.command.args.ArgumentParseException;
import org.cantaloupe.command.args.CommandArgs;
import org.cantaloupe.command.args.CommandContext;
import org.cantaloupe.command.args.parsing.SingleArg;
import org.cantaloupe.plugin.CantaloupePlugin;
import org.cantaloupe.text.Text;

public class CommandEntry {
    private CantaloupePlugin owner   = null;
    private CommandSpec      spec    = null;
    private String           name    = null;
    private List<String>     aliases = null;
    private Command          handle  = null;

    protected CommandEntry(CantaloupePlugin owner, CommandSpec spec, String name, List<String> aliases) {
        this.owner = owner;
        this.spec = spec;
        this.name = name;
        this.aliases = aliases;

        this.createHandle();
    }

    private void createHandle() {
        String description = this.getDescription().isPresent() ? this.getDescription().get().toLegacy() : "";
        String usage = this.getUsage().isPresent() ? this.getUsage().get().toLegacy() : "";

        this.handle = new Command(this.name, description, usage, this.aliases) {
            @Override
            public boolean execute(CommandSender sender, String command, String[] arguments) {
                CommandSource source = new CommandSource(sender);

                if (spec.getPermission() != null) {
                    if (!source.hasPermission(spec.getPermission())) {
                        if (spec.getErrors().containsKey(ErrorType.NO_PERMS)) {
                            source.sendMessage(spec.getErrors().get(ErrorType.NO_PERMS));
                        } else {
                            source.sendMessage("You do not have the required permissions to execute that command.");
                        }

                        return true;
                    }
                }

                if (arguments.length == 0) {
                    if (spec.getExecutor() == null) {
                        return true;
                    }

                    CommandContext context = processArguments(source, arguments);
                    if (context == null) {
                        return true;
                    }

                    return spec.getExecutor().execute(source, context) == CommandResult.SUCCESS;
                } else if (arguments.length >= 1) {
                    CommandChild child = getChild(arguments[0]);

                    if (child != null) {
                        return child.execute(source, Arrays.copyOfRange(arguments, 1, arguments.length));
                    } else {
                        if (spec.getExecutor() == null) {
                            return true;
                        }

                        CommandContext context = processArguments(source, arguments);
                        if (context == null) {
                            return true;
                        }

                        return spec.getExecutor().execute(source, context) == CommandResult.SUCCESS;
                    }
                }

                return false;
            }
        };
    }

    private CommandContext processArguments(CommandSource source, String[] args) {
        CommandContext context = new CommandContext();
        ArrayList<SingleArg> arguments = new ArrayList<SingleArg>();

        for (int i = 0; i < args.length; i++) {
            arguments.add(new SingleArg(args[i], i, i + 1));
        }

        try {
            this.spec.getArguments().parse(source, new CommandArgs(this.spec, String.join(" ", args), arguments), context);

            return context;
        } catch (ArgumentParseException e) {
            source.sendMessage(e.getText());

            return null;
        }
    }

    private CommandChild getChild(String argument) {
        for (CommandChild child : this.spec.getChildren()) {
            if (child.getName().equals(argument) || child.getAliases().contains(argument)) {
                return child;
            }
        }

        return null;
    }

    protected CantaloupePlugin getOwner() {
        return this.owner;
    }

    protected String getName() {
        return this.name;
    }

    protected List<String> getAliases() {
        return this.aliases;
    }

    protected String getPermission() {
        return this.spec.getPermission();
    }

    protected Optional<Text> getUsage() {
        return this.spec.getUsage();
    }

    protected Optional<Text> getHelp() {
        return this.spec.getHelp();
    }

    protected Optional<Text> getDescription() {
        return this.spec.getDescription();
    }

    protected Command getHandle() {
        return this.handle;
    }
}