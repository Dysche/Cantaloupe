package org.cantaloupe.command;

import org.cantaloupe.text.Text;
import org.cantaloupe.util.TextMessageException;

public class CommandException extends TextMessageException {
    private static final long serialVersionUID = 4626722485860074825L;

    private final boolean includeUsage;

    public CommandException(Text message) {
        this(message, false);
    }

    public CommandException(Text message, Throwable cause) {
        this(message, cause, false);
    }

    public CommandException(Text message, boolean includeUsage) {
        super(message);
        this.includeUsage = includeUsage;
    }

    public CommandException(Text message, Throwable cause, boolean includeUsage) {
        super(message, cause);
        this.includeUsage = includeUsage;
    }

    public boolean shouldIncludeUsage() {
        return this.includeUsage;
    }
}