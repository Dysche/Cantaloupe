package org.cantaloupe.util;

import org.cantaloupe.text.Text;

public class TextMessageException extends Exception {
    private static final long serialVersionUID = -5281221645176698853L;
    private final Text message;

    public TextMessageException() {
        this.message = null;
    }

    public TextMessageException(Text message) {
        this.message = message;
    }

    public TextMessageException(Text message, Throwable throwable) {
        super(throwable);
        this.message = message;
    }

    public TextMessageException(Throwable throwable) {
        super(throwable);
        this.message = null;
    }

    @Override
    public String getMessage() {
        Text message = getText();
        
        return message == null ? null : message.toPlain();
    }

    public Text getText() {
        return this.message;
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
}