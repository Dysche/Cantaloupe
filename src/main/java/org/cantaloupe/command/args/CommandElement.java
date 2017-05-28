package org.cantaloupe.command.args;

import java.util.List;

import org.cantaloupe.command.CommandSource;
import org.cantaloupe.text.Text;

public abstract class CommandElement {
    private final String key;

    protected CommandElement(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        Object val = parseValue(source, args);

        if (this.key != null && val != null) {
            if (val instanceof Iterable<?>) {
                for (Object ent : ((Iterable<?>) val)) {
                    context.putArg(this.key, ent);
                }
            } else {
                context.putArg(this.key, val);
            }
        }
    }

    protected abstract Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException;

    public abstract List<String> complete(CommandSource src, CommandArgs args, CommandContext context);

    public Text getUsage(CommandSource src) {
        return getKey() == null ? Text.of() : Text.of("<", this.key, ">");
    }
}