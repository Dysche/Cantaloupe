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

/**
 * A class containing all information about a command.
 * 
 * @author Dylan Scheltens
 *
 */
public class CommandSpec {
    private final String               permission;
    private final Optional<Text>       usage;
    private final Optional<Text>       description;
    private final CommandElement       args;
    private final ICommandExecutor     executor;
    private final Map<ErrorType, Text> errors;
    private final List<CommandChild>   children;

    private CommandSpec(String permission, Text usage, Text description, CommandElement args, ICommandExecutor executor, Map<ErrorType, Text> errors, Map<CommandSpec, List<String>> children) {
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

    /**
     * Creates and returns a new builder.
     * 
     * @return The builder
     */
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

    protected ICommandExecutor getExecutor() {
        return this.executor;
    }

    public Map<ErrorType, Text> getErrors() {
        return this.errors;
    }

    protected List<CommandChild> getChildren() {
        return this.children;
    }

    /**
     * A class used to build a command.
     * 
     * @author Dylan Scheltens
     *
     */
    public static final class Builder {
        private static final CommandElement    DEFAULT_ARG = GeneralArguments.none();
        private String                         permission  = null;
        private Text                           usage       = null;
        private Text                           description = null;
        private CommandElement                 args        = DEFAULT_ARG;
        private ICommandExecutor               executor    = null;
        private Map<ErrorType, Text>           errors      = null;
        private Map<CommandSpec, List<String>> children    = null;

        /**
         * Sets the permission node of the builder.
         * 
         * @param permission
         *            The permission node
         * @return The builder
         */
        public Builder permission(String permission) {
            this.permission = permission;

            return this;
        }

        /**
         * Sets the description of the builder.
         * 
         * @param description
         *            The description
         * @return The builder
         */
        public Builder description(Text description) {
            this.description = description;

            return this;
        }

        /**
         * Sets the usage of the builder.
         * 
         * @param usage
         *            The usage
         * @return The builder
         */
        public Builder usage(Text usage) {
            this.usage = usage;

            return this;
        }

        /**
         * Sets the argument of the builder.
         * 
         * @param arg
         *            The argument
         * @return The builder
         */
        public Builder argument(CommandElement arg) {
            this.args = arg;

            return this;
        }

        /**
         * Sets the arguments of the builder.
         * 
         * @param args
         *            The arguments
         * @return The builder
         */
        public Builder arguments(CommandElement... args) {
            this.args = GeneralArguments.seq(args);

            return this;
        }

        /**
         * Sets the executor of the builder.
         * 
         * @param executor
         *            The executor
         * @return The builder
         */
        public Builder executor(ICommandExecutor executor) {
            this.executor = executor;

            return this;
        }

        /**
         * Sets an error message for the builder.
         * 
         * @param type
         *            The type of error
         * @param message
         *            The message
         * @return The builder
         */
        public Builder error(ErrorType type, Text message) {
            if (this.errors == null) {
                this.errors = new HashMap<ErrorType, Text>();
            }

            this.errors.put(type, message);

            return this;
        }

        /**
         * Adds a child to the builder.
         * 
         * @param child
         *            The child
         * @param aliases
         *            An array of aliases
         * @return The builder
         */
        public Builder child(CommandSpec child, String... aliases) {
            if (this.children == null) {
                this.children = new HashMap<CommandSpec, List<String>>();
            }

            this.children.put(child, new ArrayList<String>(Arrays.asList(aliases)));

            return this;
        }

        /**
         * Adds a child to the builder.
         * 
         * @param child
         *            The child
         * @param aliases
         *            A list of aliases
         * @return The builder
         */
        public Builder child(CommandSpec child, ArrayList<String> aliases) {
            if (this.children == null) {
                this.children = new HashMap<CommandSpec, List<String>>();
            }

            this.children.put(child, aliases);

            return this;
        }

        /**
         * Creates and returns a new command spec from the builder.
         * 
         * @return The command spec
         */
        public CommandSpec build() {
            return new CommandSpec(this.permission, this.usage, this.description, this.args, this.executor, this.errors, this.children);
        }
    }

    public enum ErrorType {
        NO_PERMS, NOT_ENOUGH_ARGUMENTS, NOT_BOOLEAN, NOT_LONG, NOT_NUMBER, NOT_INTEGER
    }
}