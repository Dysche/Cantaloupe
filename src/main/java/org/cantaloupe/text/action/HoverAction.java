package org.cantaloupe.text.action;

import org.bukkit.inventory.ItemStack;
import org.cantaloupe.text.Text;

public abstract class HoverAction<R> extends TextAction<R> {
    HoverAction(R result) {
        super(result);
    }

    public static final class ShowText extends HoverAction<Text> {
        ShowText(Text text) {
            super(text);
        }
    }

    public static final class ShowItem extends HoverAction<Object> {
        ShowItem(ItemStack stack) {
            super(stack);
        }
    }
}