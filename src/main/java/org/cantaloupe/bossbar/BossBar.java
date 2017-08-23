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

public class BossBar {
    private org.bukkit.boss.BossBar handle = null;

    private BossBar(Text title, BarColor color, BarStyle style, List<BarFlag> flags) {
        List<org.bukkit.boss.BarFlag> newFlags = new ArrayList<org.bukkit.boss.BarFlag>();

        for (BarFlag flag : flags) {
            newFlags.add(org.bukkit.boss.BarFlag.valueOf(flag.name()));
        }

        this.handle = Bukkit.getServer().createBossBar(title.toLegacy(), org.bukkit.boss.BarColor.valueOf(color.name()), org.bukkit.boss.BarStyle.valueOf(style.name()), newFlags.toArray(new org.bukkit.boss.BarFlag[0]));
    }

    public static Builder builder() {
        return new Builder();
    }

    public void show() {
        for (Player player : Cantaloupe.getPlayerManager().getPlayers()) {
            this.handle.addPlayer(player.toHandle());
        }
    }

    public void hide() {
        this.handle.removeAll();
    }

    public void showFor(Player player) {
        this.handle.addPlayer(player.toHandle());
    }

    public void hideFor(Player player) {
        this.handle.removePlayer(player.toHandle());
    }

    public boolean hasFlag(BarFlag flag) {
        return this.handle.hasFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    public void addFlag(BarFlag flag) {
        this.handle.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    public void removeFlag(BarFlag flag) {
        this.handle.removeFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    public void setText(Text title) {
        this.handle.setTitle(title.toLegacy());
    }

    public void setColor(BarColor color) {
        this.handle.setColor(org.bukkit.boss.BarColor.valueOf(color.name()));
    }

    public void setStyle(BarStyle style) {
        this.handle.setStyle(org.bukkit.boss.BarStyle.valueOf(style.name()));
    }

    public void setFlags(List<BarFlag> flags) {
        this.handle.removeFlag(org.bukkit.boss.BarFlag.CREATE_FOG);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.DARKEN_SKY);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.PLAY_BOSS_MUSIC);

        for (BarFlag flag : flags) {
            this.handle.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
        }
    }

    public void setFlags(Collection<BarFlag> flags) {
        this.handle.removeFlag(org.bukkit.boss.BarFlag.CREATE_FOG);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.DARKEN_SKY);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.PLAY_BOSS_MUSIC);

        for (BarFlag flag : flags) {
            this.handle.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
        }
    }

    public void setFlags(BarFlag[] flags) {
        this.handle.removeFlag(org.bukkit.boss.BarFlag.CREATE_FOG);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.DARKEN_SKY);
        this.handle.removeFlag(org.bukkit.boss.BarFlag.PLAY_BOSS_MUSIC);

        for (BarFlag flag : flags) {
            this.handle.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
        }
    }

    public Text getTitle() {
        return Text.fromLegacy(this.handle.getTitle());
    }

    public BarColor getColor() {
        return BarColor.valueOf(this.handle.getColor().name());
    }

    public BarStyle getStyle() {
        return BarStyle.valueOf(this.handle.getStyle().name());
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<Player>();
        
        PlayerManager manager = Cantaloupe.getPlayerManager();       
        for (org.bukkit.entity.Player player : this.handle.getPlayers()) {
            players.add(manager.getPlayerFromHandle(player).get());
        }

        return players;
    }

    public static class Builder {
        private Text          title = null;
        private BarColor      color = null;
        private BarStyle      style = null;
        private List<BarFlag> flags = null;

        private Builder() {

        }

        public Builder title(Text title) {
            this.title = title;

            return this;
        }

        public Builder color(BarColor color) {
            this.color = color;

            return this;
        }

        public Builder style(BarStyle style) {
            this.style = style;

            return this;
        }

        public Builder flag(BarFlag flag) {
            if (this.flags == null) {
                this.flags = new ArrayList<BarFlag>();
            }

            this.flags.add(flag);

            return this;
        }

        public Builder flags(List<BarFlag> flags) {
            this.flags = flags;

            return this;
        }

        public Builder flags(Collection<BarFlag> flags) {
            this.flags = new ArrayList<BarFlag>(flags);

            return this;
        }

        public Builder flags(BarFlag[] flags) {
            this.flags = Arrays.asList(flags);

            return this;
        }

        public BossBar create() {
            return new BossBar(this.title, this.color, this.style, this.flags);
        }
    }
}