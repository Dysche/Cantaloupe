package org.cantaloupe.command.args;

import static org.cantaloupe.util.CantaloupeHelper.t;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cantaloupe.command.CommandSpec;
import org.cantaloupe.command.CommandSpec.ErrorType;
import org.cantaloupe.command.args.parsing.SingleArg;
import org.cantaloupe.text.Text;

public final class CommandArgs {
    private final CommandSpec spec;
    private final String      rawInput;
    private List<SingleArg>   args;
    private int               index = -1;

    public CommandArgs(CommandSpec spec, String rawInput, List<SingleArg> args) {
        this.spec = spec;
        this.rawInput = rawInput;
        this.args = new ArrayList<>(args);
    }

    public boolean hasNext() {
        return this.index + 1 < this.args.size();
    }

    public String peek() throws ArgumentParseException {
        if (!hasNext()) {
            Map<ErrorType, Text> errors = this.spec.getErrors();
            Text error = null;

            if (errors != null) {
                error = errors.get(ErrorType.NOT_ENOUGH_ARGUMENTS);
            }

            if (!this.spec.requiresArguments()) {
                return null;
            }

            throw createError(error == null ? t("Not enough arguments") : error);
        }

        return this.args.get(this.index + 1).getValue();
    }

    public String next() throws ArgumentParseException {
        if (!hasNext()) {
            Map<ErrorType, Text> errors = this.spec.getErrors();
            Text error = null;

            if (errors != null) {
                error = errors.get(ErrorType.NOT_ENOUGH_ARGUMENTS);
            }

            if (!this.spec.requiresArguments()) {
                return null;
            }

            throw createError(error == null ? t("Not enough arguments") : error);
        }

        return this.args.get(++this.index).getValue();
    }

    public Optional<String> nextIfPresent() {
        return hasNext() ? Optional.of(this.args.get(++this.index).getValue()) : Optional.<String>empty();
    }

    public ArgumentParseException createError(Text message) {
        return new ArgumentParseException(message, this.rawInput, this.index < 0 ? 0 : this.args.get(this.index).getStartIndex());
    }

    public List<String> getAll() {
        return Collections.unmodifiableList(this.args.stream().map(SingleArg::getValue).collect(Collectors.toList()));
    }

    List<SingleArg> getArgs() {
        return this.args;
    }

    public Object getState() {
        return this.index;
    }

    public void setState(Object state) {
        if (!(state instanceof Integer)) {
            throw new IllegalArgumentException("Provided state was not of appropriate format returned by getState!");
        }

        this.index = (Integer) state;
    }

    public String getRaw() {
        return this.rawInput;
    }

    public void insertArg(String value) {
        int index = this.index < 0 ? 0 : this.args.get(this.index).getEndIndex();

        this.args.add(this.index + 1, new SingleArg(value, index, index));
    }

    public void removeArgs(Object startState, Object endState) {
        if (!(startState instanceof Integer) || !(endState instanceof Integer)) {
            throw new IllegalArgumentException("One of the states provided was not of the correct type!");
        }

        int startIdx = (Integer) startState;
        int endIdx = (Integer) endState;

        if (this.index >= startIdx) {
            if (this.index < endIdx) {
                this.index = startIdx - 1;
            } else {
                this.index -= (endIdx - startIdx) + 1;
            }
        }

        for (int i = startIdx; i <= endIdx; ++i) {
            this.args.remove(startIdx);
        }
    }

    void previous() {
        if (this.index > -1) {
            --this.index;
        }
    }

    public int getRawPosition() {
        return this.index < 0 ? 0 : this.args.get(this.index).getStartIndex();
    }

    protected CommandSpec getSpec() {
        return this.spec;
    }
}