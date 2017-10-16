package org.cantaloupe.text.format;

import net.md_5.bungee.api.ChatColor;

public class TextStyles {
    public static final TextStyle NONE          = TextStyle.of("NONE", null);
    public static final TextStyle OBFUSCATED    = TextStyle.of("OBFUSCATED", ChatColor.MAGIC);
    public static final TextStyle BOLD          = TextStyle.of("BOLD", ChatColor.BOLD);
    public static final TextStyle STRIKETHROUGH = TextStyle.of("STRIKETHROUGH", ChatColor.STRIKETHROUGH);
    public static final TextStyle UNDERLINE     = TextStyle.of("UNDERLINE", ChatColor.UNDERLINE);
    public static final TextStyle ITALIC        = TextStyle.of("ITALIC", ChatColor.ITALIC);
    public static final TextStyle RESET         = TextStyle.of("RESET", ChatColor.RESET);

    public static TextStyle of(ChatColor color) {
        TextStyle[] styles = {
                NONE, OBFUSCATED, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET
        };

        for (TextStyle s : styles) {
            if (s.toHandle() == color) {
                return s;
            }
        }

        return NONE;
    }
}