package org.cantaloupe.bossbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.player.PlayerManager;
import org.cantaloupe.text.Text;

/**
 * A class used to create a bossbar.
 * 
 * @author Dylan Scheltens
 *
 */
public class BossBar {
    private org.bukkit.boss.BossBar handle = null;

    private BossBar(Text title, BarColor color, BarStyle style, List<BarFlag> flags) {
        List<org.bukkit.boss.BarFlag> newFlags = new ArrayList<org.bukkit.boss.BarFlag>();

        for (BarFlag flag : flags) {
            newFlags.add(org.bukkit.boss.BarFlag.valueOf(flag.name()));
        }

        this.handle = Bukkit.getServer().createBossBar(title.toLegacy(), org.bukkit.boss.BarColor.valueOf(color.name()), org.bukkit.boss.BarStyle.valueOf(style.name()), newFlags.toArray(new org.bukkit.boss.BarFlag[0]));
    }

    /**
     * Creates and returns a new builder.
     * 
     * @return The builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Shows the bossbar to the players online.
     */
    public void show() {
        for (Player player : Cantaloupe.getPlayerManager().getPlayers()) {
            this.handle.addPlayer(player.toHandle());
        }
    }

    /**
     * Hides the bossbar from the players online.
     */
    public void hide() {
        this.handle.removeAll();
    }

    /**
     * Shows the bossbar for the player.
     * 
     * @param player
     *            The player
     */
    public void showFor(Player player) {
        this.handle.addPlayer(player.toHandle());
    }

    /**
     * Hides the bossbar from the player.
     * 
     * @param player
     *            The player
     */
    public void hideFor(Player player) {
        this.handle.removePlayer(player.toHandle());
    }

    /**
     * Adds a flag to the bossbar.
     * 
     * @param flag
     *            The flag
     */
    public void addFlag(BarFlag flag) {
        this.handle.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    /**
     * Removes a flag from the bossbar.
     * 
     * @param flag
     *            The flag
     */
    public void removeFlag(BarFlag flag) {
        this.handle.removeFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    /**
     * Sets the title of the bossbar.
     * 
     * @param title
     *            The title
     */
    public void setTitle(Text title) {
        this.handle.setTitle(title.toLegacy());
    }

    /**
     * Sets the color of the bossbar.
     * 
     * @param color
     *            The color
     */
    public void setColor(BarColor color) {
        this.handle.setColor(org.bukkit.boss.BarColor.valueOf(color.name()));
    }

    /**
     * Sets the style of the bossbar.
     * 
     * @param style
     *            The style
     */
    public void setStyle(BarStyle style) {
        this.handle.setStyle(org.bukkit.boss.BarStyle.valueOf(style.name()));
    }

    /**
     * Sets the flags of the bossbar.
     * 
     * @param flags
     *            A list of flags
     */
    public void setFlags(List<BarFlag> flags) {
        this.handle.removeFlag(org.bukkit.boss.BarFlag.CREATE_FOG);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.DARKEN_SKY);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.PLAY_BOSS_MUSIC);

        for (BarFlag flag : flags) {
            this.handle.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
        }
    }

    /**
     * Sets the flags of the bossbar.
     * 
     * @param flags
     *            A collection of flags
     */
    public void setFlags(Collection<BarFlag> flags) {
        this.handle.removeFlag(org.bukkit.boss.BarFlag.CREATE_FOG);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.DARKEN_SKY);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.PLAY_BOSS_MUSIC);

        for (BarFlag flag : flags) {
            this.handle.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
        }
    }

    /**
     * Sets the flags of the bossbar.
     * 
     * @param flags
     *            An array of flags
     */
    public void setFlags(BarFlag[] flags) {
        this.handle.removeFlag(org.bukkit.boss.BarFlag.CREATE_FOG);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.DARKEN_SKY);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.PLAY_BOSS_MUSIC);

        for (BarFlag flag : flags) {
            this.handle.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
        }
    }

    /**
     * Checks if the bossbar has a flag set.
     * 
     * @param flag
     *            The flag
     * @return True if it's set, false if not
     */
    public boolean hasFlag(BarFlag flag) {
        return this.handle.hasFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    /**
     * Returns the handle of the bossbar.
     * 
     * @return The handle
     */
    public org.bukkit.boss.BossBar toHandle() {
        return this.handle;
    }

    /**
     * Gets the title of the bossbar.
     * 
     * @return The title
     */
    public Text getTitle() {
        return Text.fromLegacy(this.handle.getTitle());
    }

    /**
     * Gets the color of the bossbar.
     * 
     * @return The color
     */
    public BarColor getColor() {
        return BarColor.valueOf(this.handle.getColor().name());
    }

    /**
     * Gets the style of the bossbar.
     * 
     * @return The style
     */
    public BarStyle getStyle() {
        return BarStyle.valueOf(this.handle.getStyle().name());
    }

    /**
     * Gets the list of players who can see the bossbar.
     * 
     * @return The list of players
     */
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<Player>();

        PlayerManager manager = Cantaloupe.getPlayerManager();
        for (org.bukkit.entity.Player player : this.handle.getPlayers()) {
            players.add(manager.getPlayerFromHandle(player).get());
        }

        return players;
    }

    /**
     * A class used to build a bossbar.
     * 
     * @author Dylan Scheltens
     *
     */
    public static class Builder {
        private Text          title = null;
        private BarColor      color = null;
        private BarStyle      style = null;
        private List<BarFlag> flags = null;

        private Builder() {

        }

        /**
         * Sets the title of the builder.
         * 
         * @param title
         *            The title
         * @return The builder
         */
        public Builder title(Text title) {
            this.title = title;

            return this;
        }

        /**
         * Sets the color of the builder.
         * 
         * @param color
         *            The color
         * @return The builder
         */
        public Builder color(BarColor color) {
            this.color = color;

            return this;
        }

        /**
         * Sets the style of the builder.
         * 
         * @param style
         *            The style
         * @return The builder
         */
        public Builder style(BarStyle style) {
            this.style = style;

            return this;
        }

        /**
         * Adds a flag to the builder.
         * 
         * @param flag
         *            The flag
         * @return The builder
         */
        public Builder flag(BarFlag flag) {
            if (this.flags == null) {
                this.flags = new ArrayList<BarFlag>();
            }

            this.flags.add(flag);

            return this;
        }

        /**
         * Sets the flags of the builder.
         * 
         * @param flags
         *            A list of flags
         * @return The builder
         */
        public Builder flags(List<BarFlag> flags) {
            this.flags = flags;

            return this;
        }

        /**
         * Sets the flags of the builder.
         * 
         * @param flags
         *            A collection of flags
         * @return The builder
         */
        public Builder flags(Collection<BarFlag> flags) {
            this.flags = new ArrayList<BarFlag>(flags);

            return this;
        }

        /**
         * Sets the flags of the builder.
         * 
         * @param flags
         *            An array of flags
         * @return The builder
         */
        public Builder flags(BarFlag[] flags) {
            this.flags = Arrays.asList(flags);

            return this;
        }

        /**
         * Creates and returns a new bossbar from the builder.
         * 
         * @return The bossbar
         */
        public BossBar create() {
            return new BossBar(this.title, this.color, this.style, this.flags);
        }
    }
}