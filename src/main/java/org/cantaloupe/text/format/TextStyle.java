package org.cantaloupe.text.format;

import net.md_5.bungee.api.ChatColor;

public class TextStyle {
    private String    ID = null;
    private ChatColor handle;

    private TextStyle(String ID, ChatColor handle) {
        this.ID = ID;
        this.handle = handle;
    }

    public static TextStyle of(String ID, ChatColor handle) {
        return new TextStyle(ID, handle);
    }

    public String getID() {
        return this.ID;
    }

    public ChatColor getHandle() {
        return this.handle;
    }
}