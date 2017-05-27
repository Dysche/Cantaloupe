package org.cantaloupe.text.action;

import java.net.URL;
import java.util.function.Consumer;

public abstract class ClickAction<R> extends TextAction<R> {
    ClickAction(R result) {
        super(result);
    }

    public static final class OpenUrl extends ClickAction<URL> {
        OpenUrl(URL url) {
            super(url);
        }
    }

    public static final class RunCommand extends ClickAction<String> {
        RunCommand(String command) {
            super(command);
        }
    }
    
    public static final class ChangePage extends ClickAction<Integer> {
        ChangePage(int page) {
            super(page);
        }
    }

    public static final class SuggestCommand extends ClickAction<String> {
        SuggestCommand(String command) {
            super(command);
        }
    }

    public static final class ExecuteCallback extends ClickAction<Consumer<?>> {
        ExecuteCallback(Consumer<String> result) {
            super(result);
        }
    }
}