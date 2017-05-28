package org.cantaloupe.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cantaloupe.command.args.CommandElement;
import org.cantaloupe.command.args.GeneralArguments;
import org.cantaloupe.text.Text;

public class CommandSpec {
    private final String               permission;
    private final Optional<Text>       usage;
    private final Optional<Text>       description;
    private final CommandElement       args;
    private final CommandExecutor      executor;
    private final Map<ErrorType, Text> errors;
    private final List<CommandChild>   children;

    private CommandSpec(String permission, Text usage, Text description, CommandElement args, CommandExecutor executor, Map<ErrorType, Text> errors, Map<CommandSpec, List<String>> children) {
        this.permission = permission;
        this.usage = Optional.ofNullable(usage);
        this.description = Optional.ofNullable(description);
        this.args = args;
        this.executor = executor;
        this.errors = errors;

        this.children = new ArrayList<CommandChild>();

        if (children != null) {
            for (CommandSpec key : children.keySet()) {
                List<String> aliases = children.get(key);

                this.children.add(new CommandChild(this, key, aliases.get(0), aliases.subList(1, aliases.size())));
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    protected String getPermission() {
        return this.permission;
    }

    protected Optional<Text> getUsage() {
        return this.usage;
    }

    protected Optional<Text> getHelp() {
        Text text = Text.of();

        Optional<Text> description = this.getDescription();
        if (description.isPresent()) {
            text.append(description.get(), Text.NEW_LINE);
        }

        Optional<Text> usage = this.getDescription();
        if (usage.isPresent()) {
            text.append(usage.get(), Text.NEW_LINE);
        }

        return Optional.of(text);
    }

    protected Optional<Text> getDescription() {
        return this.description;
    }

    protected CommandElement getArguments() {
        return this.args;
    }

    protected CommandExecutor getExecutor() {
        return this.executor;
    }

    public Map<ErrorType, Text> getErrors() {
        return this.errors;
    }

    protected List<CommandChild> getChildren() {
        return this.children;
    }

    public static final class Builder {
        private static final CommandElement    DEFAULT_ARG = GeneralArguments.none();
        private String                         permission  = null;
        private Text                           usage       = null;
        private Text                           description = null;
        private CommandElement                 args        = DEFAULT_ARG;
        private CommandExecutor                executor    = null;
        private Map<ErrorType, Text>           errors      = null;
        private Map<CommandSpec, List<String>> children    = null;

        public Builder permission(String permission) {
            this.permission = permission;

            return this;
        }

        public Builder description(Text description) {
            this.description = description;

            return this;
        }

        public Builder usage(Text usage) {
            this.usage = usage;

            return this;
        }

        public Builder arguments(CommandElement args) {
            this.args = args;

            return this;
        }

        public Builder arguments(CommandElement... args) {
            this.args = GeneralArguments.seq(args);

            return this;
        }

        public Builder executor(CommandExecutor executor) {
            this.executor = executor;

            return this;
        }

        public Builder error(ErrorType type, Text message) {
            if (this.errors == null) {
                this.errors = new HashMap<ErrorType, Text>();
            }

            this.errors.put(type, message);

            return this;
        }

        public Builder child(CommandSpec child, String... aliases) {
            if (this.children == null) {
                this.children = new HashMap<CommandSpec, List<String>>();
            }

            this.children.put(child, new ArrayList<String>(Arrays.asList(aliases)));

            return this;
        }

        public Builder child(CommandSpec child, ArrayList<String> aliases) {
            if (this.children == null) {
                this.children = new HashMap<CommandSpec, List<String>>();
            }

            this.children.put(child, aliases);

            return this;
        }

        public CommandSpec build() {
            return new CommandSpec(this.permission, this.usage, this.description, this.args, this.executor, this.errors, this.children);
        }
    }

    public enum ErrorType {
        NOT_ENOUGH_ARGUMENTS, NOT_BOOLEAN, NOT_LONG, NOT_NUMBER, NOT_INTEGER
    }
}