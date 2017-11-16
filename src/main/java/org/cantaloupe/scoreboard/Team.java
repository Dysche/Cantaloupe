package org.cantaloupe.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;

/**
 * A class used to create a scoreboard team.
 * 
 * @author Dylan Scheltens
 *
 */
public class Team {
    private final org.bukkit.scoreboard.Team handle;
    private final List<String>               entries;

    protected Team(org.bukkit.scoreboard.Team handle) {
        this.handle = handle;
        this.entries = new ArrayList<String>();
    }

    /**
     * Adds a player to the team.
     * 
     * @param player
     *            The player
     */
    public void addPlayer(Player player) {
        this.addEntry(player.getName());
    }

    /**
     * Adds an entry to the team.
     * 
     * @param name
     *            The entity
     */
    public void addEntry(String entry) {
        this.handle.addEntry(entry);
        this.entries.add(entry);
    }

    /**
     * Removes a player from the team.
     * 
     * @param player
     *            The player
     */
    public void removePlayer(Player player) {
        this.removeEntry(player.getName());
    }

    /**
     * Removes an entry from the team.
     * 
     * @param entry
     *            The entry
     */
    public void removeEntry(String entry) {
        this.handle.removeEntry(entry);
        this.entries.remove(entry);
    }

    /**
     * Checks if the team has a player.
     * 
     * @param player
     *            The player
     * 
     * @return True if it does, false if not
     */
    public boolean hasPlayer(Player player) {
        return this.hasEntry(player.getName());
    }

    /**
     * Checks if the team has an entry.
     * 
     * @param entry
     *            The entry
     * 
     * @return True if it does, false if not
     */
    public boolean hasEntry(String entry) {
        return this.entries.contains(entry);
    }

    /**
     * Sets the display name of the team.
     * 
     * @param displayName
     *            The display name
     */
    public void setDisplayName(Text displayName) {
        this.handle.setDisplayName(displayName.toLegacy());
    }

    /**
     * Sets the prefix of the team.
     * 
     * @param prefix
     *            The prefix
     */
    public void setPrefix(Text prefix) {
        this.handle.setPrefix(prefix.toLegacy());
    }

    /**
     * Sets the suffix of the team.
     * 
     * @param suffix
     *            The suffix
     */
    public void setSuffix(Text suffix) {
        this.handle.setSuffix(suffix.toLegacy());
    }

    /**
     * Sets an option of the team.
     * 
     * @param option
     *            The option
     * @param status
     *            The option status
     */
    public void setOption(Option option, OptionStatus status) {
        this.handle.setOption(org.bukkit.scoreboard.Team.Option.valueOf(option.name()), org.bukkit.scoreboard.Team.OptionStatus.valueOf(status.name()));
    }

    /**
     * Sets whether or not the team allows friendly fire.
     * 
     * @param flag
     *            Whether or not the team allows friendly fire
     */
    public void setAllowFriendlyFire(boolean flag) {
        this.handle.setAllowFriendlyFire(flag);
    }

    /**
     * Sets whether or not the team can see invisible team mates.
     * 
     * @param flag
     *            Whether or not the team can see invisible team mates.
     */
    public void setCanSeeFriendlyInvisibles(boolean flag) {
        this.handle.setCanSeeFriendlyInvisibles(flag);
    }

    /**
     * Sets the color of the team.
     * 
     * @param color
     *            The color
     */
    public void setColor(ChatColor color) {
        this.handle.setColor(color);
    }

    public boolean allowFriendlyFire() {
        return this.handle.allowFriendlyFire();
    }

    public boolean canSeeFriendlyInvisibles() {
        return this.handle.canSeeFriendlyInvisibles();
    }

    /**
     * Gets the name of the team.
     * 
     * @return The name
     */
    public String getName() {
        return this.handle.getName();
    }

    /**
     * Gets the display name of the team.
     * 
     * @return The display name
     */
    public Text getDisplayName() {
        return Text.fromLegacy(this.handle.getDisplayName());
    }

    /**
     * Gets the prefix of the team.
     * 
     * @return The prefix
     */
    public Text getPrefix() {
        return Text.fromLegacy(this.handle.getPrefix());
    }

    /**
     * Gets the suffix of the team.
     * 
     * @return The suffix
     */
    public Text getSuffix() {
        return Text.fromLegacy(this.handle.getSuffix());
    }

    /**
     * Gets a the option status of an option for the team.
     * 
     * @param option
     *            The option
     * 
     * @return The option status
     */
    public OptionStatus getOption(Option option) {
        return OptionStatus.valueOf(this.handle.getOption(org.bukkit.scoreboard.Team.Option.valueOf(option.name())).name());
    }

    /**
     * Gets the color of the team.
     * 
     * @return The color
     */
    public ChatColor getColor() {
        return this.handle.getColor();
    }

    /**
     * Gets a list of players from the team.
     * 
     * @return The list of players
     */
    public List<String> getEntries() {
        return this.entries;
    }

    /**
     * Gets a list of players from the team.
     * 
     * @return The list of players
     */
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<Player>();

        for (String entry : this.entries) {
            Cantaloupe.getPlayerManager().tryGetPlayer(entry).ifPresent(player -> {
                players.add(player);
            });
        }

        return players;
    }

    protected org.bukkit.scoreboard.Team toHandle() {
        return this.handle;
    }

    /**
     * An enum containing the possible options for a team.
     * 
     * @author Dylan Scheltens
     * 
     */
    public static enum Option {
        COLLISION, DEATH_MESSAGE_VISIBILITY, NAME_TAG_VISIBILITY
    }

    /**
     * An enum containing the possible option statuses for a team.
     * 
     * @author Dylan Scheltens
     * 
     */
    public static enum OptionStatus {
        ALWAYS, FOR_OTHER_TEAMS, FOR_OWN_TEAM, NEVER
    }
}