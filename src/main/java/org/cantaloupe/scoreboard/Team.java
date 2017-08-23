package org.cantaloupe.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;

public class Team {
    private final org.bukkit.scoreboard.Team handle;
    private final List<Player>               players;

    protected Team(org.bukkit.scoreboard.Team handle) {
        this.handle = handle;
        this.players = new ArrayList<Player>();
    }

    public void addPlayer(Player player) {
        this.handle.addEntry(player.getName());
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.handle.removeEntry(player.getName());
        this.players.remove(player);
    }

    public boolean hasPlayer(Player player) {
        return this.players.contains(player);
    }

    public void setDisplayName(Text displayName) {
        this.handle.setDisplayName(displayName.toLegacy());
    }

    public void setPrefix(Text prefix) {
        this.handle.setPrefix(prefix.toLegacy());
    }

    public void setSuffix(Text suffix) {
        this.handle.setSuffix(suffix.toLegacy());
    }

    public void setOption(Option option, OptionStatus status) {
        this.handle.setOption(org.bukkit.scoreboard.Team.Option.valueOf(option.name()), org.bukkit.scoreboard.Team.OptionStatus.valueOf(option.name()));
    }

    public void setAllowFriendlyFire(boolean enabled) {
        this.handle.setAllowFriendlyFire(enabled);
    }

    public void setCanSeeFriendlyInvisibles(boolean enabled) {
        this.handle.setCanSeeFriendlyInvisibles(enabled);
    }

    public void setColor(ChatColor color) {
        this.handle.setColor(color);
    }

    public String getName() {
        return this.handle.getName();
    }

    public Text getDisplayName() {
        return Text.fromLegacy(this.handle.getDisplayName());
    }

    public Text getPrefix() {
        return Text.fromLegacy(this.handle.getPrefix());
    }

    public Text getSuffix() {
        return Text.fromLegacy(this.handle.getSuffix());
    }

    public OptionStatus getOption(Option option) {
        return OptionStatus.valueOf(this.handle.getOption(org.bukkit.scoreboard.Team.Option.valueOf(option.name())).name());
    }

    public boolean allowFriendlyFire() {
        return this.handle.allowFriendlyFire();
    }

    public boolean canSeeFriendlyInvisibles() {
        return this.handle.canSeeFriendlyInvisibles();
    }

    public ChatColor getColor() {
        return this.handle.getColor();
    }

    public Collection<Player> getPlayers() {
        return this.players;
    }

    protected org.bukkit.scoreboard.Team toHandle() {
        return this.handle;
    }

    public static enum Option {
        COLLISION, DEATH_MESSAGE_VISIBILITY, NAME_TAG_VISIBILITY
    }

    public static enum OptionStatus {
        ALWAYS, FOR_OTHER_TEAMS, FOR_OWN_TEAM, NEVER
    }
}