package org.cantaloupe.audio.sources;

import java.util.List;

import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.audio.network.packets.S004PacketPlay;
import org.cantaloupe.audio.network.packets.S005PacketPlayBounds;
import org.cantaloupe.audio.network.packets.S006PacketStop;
import org.cantaloupe.audio.network.packets.S007PacketPause;
import org.cantaloupe.audio.network.packets.S008PacketResume;
import org.cantaloupe.audio.sound.Sound;
import org.cantaloupe.audio.source.ISource;
import org.cantaloupe.audio.source.SourcePlayerSettings;
import org.cantaloupe.audio.source.SourceSettings;
import org.cantaloupe.player.Player;
import org.cantaloupe.util.DataUtils;

/**
 * A virtual source only audible by one or more players.
 * 
 * @author Dylan Scheltens
 *
 */
public class LocalSource implements ISource {
    private final String         ID;
    private SourceSettings       settings = null;
    private Sound                sound    = null;
    private int                  volume   = 100;

    protected final List<Player> players;

    protected LocalSource(String ID) {
        this.ID = ID;
        this.players = DataUtils.newArrayList();
    }

    @Override
    public void clear() {
        for (Player player : this.players) {
            this.stop(player);
        }

        this.players.clear();
    }

    /**
     * Plays the source for the player.
     * 
     * @param player
     *            The player
     */
    public void play(Player player) {
        AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

        if (wrapper.isConnected()) {
            if (wrapper.isConnected()) {
                if (!wrapper.hasSourceSettings(this)) {
                    wrapper.getConnection().sendPacket(S004PacketPlay.of(this, this.volume, 0));
                    wrapper.addSourceSettings(SourcePlayerSettings.of(this, this.volume, 0, true, false));

                    this.players.add(player);
                }
            }
        }
    }

    /**
     * Plays the source for the player within bounds.
     * 
     * @param player
     *            The player
     * @param begin
     *            The begin time of the sound in milliseconds
     * @param end
     *            The end time of the sound in milliseconds
     */
    public void playBounds(Player player, int begin, int end) {
        AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

        if (wrapper.isConnected()) {
            if (wrapper.isConnected()) {
                if (!wrapper.hasSourceSettings(this)) {
                    wrapper.getConnection().sendPacket(S005PacketPlayBounds.of(this, this.volume, 0, begin, end));
                    wrapper.addSourceSettings(SourcePlayerSettings.of(this, this.volume, 0, true, false));

                    this.players.add(player);
                }
            }
        }
    }

    /**
     * Stops the source for the player.
     * 
     * @param player
     *            The player
     */
    public void stop(Player player) {
        AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

        if (wrapper.isConnected()) {
            if (wrapper.isConnected()) {
                SourcePlayerSettings sourceSettings = wrapper.getSourceSettings(this.getID());

                if (sourceSettings.isPlaying() || sourceSettings.isPaused()) {
                    wrapper.getConnection().sendPacket(S006PacketStop.of(this));
                    wrapper.removeSourceSettings(this.getID());

                    this.players.remove(player);
                }
            }
        }
    }

    /**
     * Pauses the source for the player.
     * 
     * @param player
     *            The player
     */
    public void pause(Player player) {
        AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

        if (wrapper.isConnected()) {
            if (wrapper.isConnected()) {
                SourcePlayerSettings sourceSettings = wrapper.getSourceSettings(this.getID());

                if (sourceSettings.isPlaying()) {
                    wrapper.getConnection().sendPacket(S007PacketPause.of(this));

                    sourceSettings.setPaused(true);
                }
            }
        }
    }

    /**
     * Resumes the source for the player.
     * 
     * @param player
     *            The player
     */
    public void resume(Player player) {
        AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

        if (wrapper.isConnected()) {
            if (wrapper.isConnected()) {
                SourcePlayerSettings sourceSettings = wrapper.getSourceSettings(this.getID());

                if (sourceSettings.isPaused()) {
                    wrapper.getConnection().sendPacket(S008PacketResume.of(this));

                    sourceSettings.setPaused(false);
                }
            }
        }
    }

    @Override
    public void removePlayerInt(Player player) {
        this.players.remove(player);
    }

    /**
     * Checks if the player can hear the source.
     * 
     * @param player
     *            The player
     * @return True if the player can, false if not
     */
    public boolean hasPlayer(Player player) {
        return this.players.contains(player);
    }

    @Override
    public void setSettings(SourceSettings settings) {
        this.settings = settings;
    }

    @Override
    public void setSound(Sound sound, boolean update) {
        this.sound = sound;

        if (update) {
            for (Player player : this.players) {
                this.stop(player);
                this.play(player);
            }
        }
    }

    @Override
    public void setSound(Sound sound) {
        this.setSound(sound, false);
    }

    /**
     * Sets the volume of the source.
     * 
     * @param volume
     *            The volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String getID() {
        return this.ID;
    }

    @Override
    public SourceSettings getSettings() {
        return this.settings;
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    /**
     * Gets the volume of the source.
     * 
     * @return The volume
     */
    public int getVolume() {
        return this.volume;
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }
}