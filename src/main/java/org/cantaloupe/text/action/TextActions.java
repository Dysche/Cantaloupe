package org.cantaloupe.text.action;

import java.util.function.Consumer;

import org.bukkit.inventory.ItemStack;
import org.cantaloupe.text.Text;

public final class TextActions {
    private TextActions() {}

    public static ClickAction.OpenUrl openUrl(String url) {
        return new ClickAction.OpenUrl(url);
    }

    public static ClickAction.RunCommand runCommand(String command) {
        return new ClickAction.RunCommand(command);
    }

    public static ClickAction.ChangePage changePage(int page) {
        return new ClickAction.ChangePage(page);
    }

    public static ClickAction.SuggestCommand suggestCommand(String command) {
        return new ClickAction.SuggestCommand(command);
    }

    public static ClickAction.ExecuteCallback executeCallback(Consumer<String> callback) {
        return new ClickAction.ExecuteCallback(callback);
    }

    public static HoverAction.ShowText showText(Text text) {
        return new HoverAction.ShowText(text);
    }

    public static HoverAction.ShowItem showItem(ItemStack stack) {
        return new HoverAction.ShowItem(stack);
    }
}