package org.cantaloupe.command.args;

import static org.cantaloupe.util.CantaloupeHelper.t;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.command.CommandSource;
import org.cantaloupe.command.CommandSpec.ErrorType;
import org.cantaloupe.text.Text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A class containing all possible argument types for a command.
 * 
 * @author Dylan Scheltens
 *
 */
public class GeneralArguments {
    private static final CommandElement       NONE            = new SequenceCommandElement(ImmutableList.<CommandElement>of());
    private static final Map<String, Boolean> BOOLEAN_CHOICES = ImmutableMap.<String, Boolean>builder().put("true", true).put("t", true).put("y", true).put("yes", true).put("verymuchso", true).put("1", true).put("false", false).put("f", false).put("n", false).put("no", false).put("notatall", false).put("0", false)
            .build();

    /**
     * Returns an empty command element.
     * 
     * @return The element
     */
    public static CommandElement none() {
        return NONE;
    }

    /**
     * Creates and returns a "long" command element.
     * 
     * @param key
     *            The key of the element
     * @return The element
     */
    public static CommandElement longNum(String key) {
        return new NumericElement<>(key, Long::parseLong, Long::parseLong, ErrorType.NOT_LONG);
    }

    /**
     * Creates and returns a "double" command element.
     * 
     * @param key
     *            The key of the element
     * @return The element
     */
    public static CommandElement doubleNum(String key) {
        return new NumericElement<>(key, Double::parseDouble, null, ErrorType.NOT_NUMBER);
    }

    /**
     * Creates and returns a "integer" command element.
     * 
     * @param key
     *            The key of the element
     * @return The element
     */
    public static CommandElement integer(String key) {
        return new NumericElement<>(key, Integer::parseInt, Integer::parseInt, ErrorType.NOT_INTEGER);
    }

    /**
     * Creates and returns a "string" command element.
     * 
     * @param key
     *            The key of the element
     * @return The element
     */
    public static CommandElement string(String key) {
        return new StringElement(key);
    }

    /**
     * Creates and returns a "boolean" command element.
     * 
     * @param key
     *            The key of the element
     * @return The element
     */
    public static CommandElement bool(String key) {
        return new BooleanElement(key);
    }

    /**
     * Creates and returns a "player" command element.
     * 
     * @param key
     *            The key of the element
     * @return The element
     */
    public static CommandElement player(String key) {
        return new PlayerCommandElement(key, false);
    }

    /**
     * Creates and returns a "player" or "source" command element depending on
     * the source type.
     * 
     * @param key
     *            The key of the element
     * @return The element
     */
    public static CommandElement playerOrSource(String key) {
        return new PlayerCommandElement(key, true);
    }

    /**
     * Sequences several command elements into one.
     * 
     * @param elements
     *            The elements
     * @return The element
     */
    public static CommandElement seq(CommandElement... elements) {
        return new SequenceCommandElement(ImmutableList.copyOf(elements));
    }

    private static class SequenceCommandElement extends CommandElement {
        private final List<CommandElement> elements;

        SequenceCommandElement(List<CommandElement> elements) {
            super(null);
            this.elements = elements;
        }

        @Override
        public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
            for (CommandElement element : this.elements) {
                element.parse(source, args, context);
            }
        }

        @Override
        protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
            return null;
        }

        @Override
        public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
            for (Iterator<CommandElement> it = this.elements.iterator(); it.hasNext();) {
                CommandElement element = it.next();
                Object startState = args.getState();

                try {
                    element.parse(src, args, context);
                    Object endState = args.getState();

                    if (!args.hasNext()) {
                        args.setState(startState);
                        List<String> inputs = element.complete(src, args, context);
                        args.previous();

                        if (!inputs.contains(args.next())) {
                            return inputs;
                        }

                        args.setState(endState);
                    }
                } catch (ArgumentParseException e) {
                    args.setState(startState);

                    return element.complete(src, args, context);
                }

                if (!it.hasNext()) {
                    args.setState(startState);
                }
            }

            return Collections.emptyList();
        }
    }

    private static class PlayerCommandElement extends KeyElement {
        private final boolean returnSource;

        protected PlayerCommandElement(String key, boolean returnSource) {
            super(key);

            this.returnSource = returnSource;
        }

        @Override
        protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
            try {
                return Cantaloupe.getPlayerManager().tryGetPlayer(args.next()).get();
            } catch (ArgumentParseException e) {
                if (this.returnSource && source instanceof Player) {
                    return source;
                }

                throw e;
            }
        }
    }

    private static class BooleanElement extends KeyElement {
        BooleanElement(String key) {
            super(key);
        }

        @Override
        public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
            final String input = args.next();

            if (BOOLEAN_CHOICES.containsKey(input)) {
                return BOOLEAN_CHOICES.get(input);
            } else {
                Map<ErrorType, Text> errors = args.getSpec().getErrors();
                Text error = null;

                if (errors != null) {
                    error = errors.get(ErrorType.NOT_BOOLEAN);
                }

                throw args.createError(error == null ? t("Expected a boolean, but input '%s' was not", input) : Text.fromLegacy(String.format(error.toLegacy(), input)));
            }
        }
    }

    private static class StringElement extends KeyElement {
        StringElement(String key) {
            super(key);
        }

        @Override
        public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
            return args.next();
        }
    }

    private static class NumericElement<T extends Number> extends KeyElement {
        private final Function<String, T>            parseFunc;
        private final BiFunction<String, Integer, T> parseRadixFunction;
        private final ErrorType                      errorType;

        protected NumericElement(String key, Function<String, T> parseFunc, BiFunction<String, Integer, T> parseRadixFunction, ErrorType errorType) {
            super(key);

            this.parseFunc = parseFunc;
            this.parseRadixFunction = parseRadixFunction;
            this.errorType = errorType;
        }

        @Override
        public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
            final String input = args.next();

            try {
                if (this.parseRadixFunction != null) {
                    if (input.startsWith("0x")) {
                        return this.parseRadixFunction.apply(input.substring(2), 16);
                    } else if (input.startsWith("0b")) {
                        return this.parseRadixFunction.apply(input.substring(2), 2);
                    }
                }

                return this.parseFunc.apply(input);
            } catch (NumberFormatException ex) {
                Map<ErrorType, Text> errors = args.getSpec().getErrors();
                Text error = null;

                if (errors != null) {
                    error = errors.get(this.errorType);
                }

                switch (this.errorType) {
                    case NOT_LONG:
                        throw args.createError(error == null ? t("Expected a long, but input '%s' was not", input) : Text.fromLegacy(String.format(error.toLegacy(), input)));
                    case NOT_NUMBER:
                        throw args.createError(error == null ? t("Expected a number, but input '%s' was not", input) : Text.fromLegacy(String.format(error.toLegacy(), input)));
                    case NOT_INTEGER:
                        throw args.createError(error == null ? t("Expected an integer, but input '%s' was not", input) : Text.fromLegacy(String.format(error.toLegacy(), input)));
                    default:
                        throw ex;
                }
            }
        }
    }

    private abstract static class KeyElement extends CommandElement {
        private KeyElement(String key) {
            super(key);
        }

        @Override
        public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
            return Collections.emptyList();
        }
    }
}