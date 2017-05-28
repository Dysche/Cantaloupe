package org.cantaloupe.text.format;

import net.md_5.bungee.api.ChatColor;

public class TextColors {
    public static final TextColor AQUA         = TextColor.of("AQUA", ChatColor.AQUA);
    public static final TextColor BLACK        = TextColor.of("BLACK", ChatColor.BLACK);
    public static final TextColor BLUE         = TextColor.of("BLUE", ChatColor.BLUE);
    public static final TextColor DARK_AQUA    = TextColor.of("DARK_AQUA", ChatColor.DARK_AQUA);
    public static final TextColor DARK_BLUE    = TextColor.of("DARK_BLUE", ChatColor.DARK_BLUE);
    public static final TextColor DARK_GRAY    = TextColor.of("DARK_GRAY", ChatColor.DARK_GRAY);
    public static final TextColor DARK_GREEN   = TextColor.of("DARK_GREEN", ChatColor.DARK_GREEN);
    public static final TextColor DARK_PURPLE  = TextColor.of("DARK_PURPLE", ChatColor.DARK_PURPLE);
    public static final TextColor DARK_RED     = TextColor.of("DARK_RED", ChatColor.DARK_RED);
    public static final TextColor GOLD         = TextColor.of("GOLD", ChatColor.GOLD);
    public static final TextColor GRAY         = TextColor.of("GRAY", ChatColor.GRAY);
    public static final TextColor GREEN        = TextColor.of("GREEN", ChatColor.GREEN);
    public static final TextColor LIGHT_PURPLE = TextColor.of("LIGHT_PURPLE", ChatColor.LIGHT_PURPLE);
    public static final TextColor NONE         = TextColor.of("NONE", null);
    public static final TextColor RED          = TextColor.of("RED", ChatColor.RED);
    public static final TextColor WHITE        = TextColor.of("WHITE", ChatColor.WHITE);
    public static final TextColor YELLOW       = TextColor.of("YELLOW", ChatColor.YELLOW);

    public static TextColor of(ChatColor color) {
        TextColor[] colors = {
                NONE, AQUA, BLACK, BLUE, DARK_AQUA, DARK_BLUE, DARK_GRAY, DARK_GREEN, DARK_PURPLE, DARK_RED, GOLD, GRAY, GREEN, LIGHT_PURPLE, RED, WHITE, YELLOW
        };

        for (TextColor c : colors) {
            if (c.getHandle() == color) {
                return c;
            }
        }

        return NONE;
    }
}