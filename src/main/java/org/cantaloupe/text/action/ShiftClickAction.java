package org.cantaloupe.text.action;

public abstract class ShiftClickAction<R> extends TextAction<R> {
    ShiftClickAction(R result) {
        super(result);
    }

    public static final class InsertText extends ShiftClickAction<String> {
        InsertText(String text) {
            super(text);
        }
    }
}