package org.cantaloupe.text.format;

import net.md_5.bungee.api.ChatColor;

public class TextColor {
    private String    ID = null;
    private ChatColor handle;

    private TextColor(String ID, ChatColor handle) {
        this.ID = ID;
        this.handle = handle;
    }

    public static TextColor of(String ID, ChatColor handle) {
        return new TextColor(ID, handle);
    }

    public String getID() {
        return this.ID;
    }

    public ChatColor getHandle() {
        return this.handle;
    }
}