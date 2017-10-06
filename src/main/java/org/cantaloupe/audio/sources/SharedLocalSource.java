package org.cantaloupe.audio.sources;

import java.util.List;

import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.audio.network.packets.S005PacketPlayBounds;
import org.cantaloupe.audio.network.packets.S006PacketStop;
import org.cantaloupe.audio.network.packets.S007PacketPause;
import org.cantaloupe.audio.network.packets.S008PacketResume;
import org.cantaloupe.audio.sound.Sound;
import org.cantaloupe.audio.source.ITimableSource;
import org.cantaloupe.audio.source.SourcePlayerSettings;
import org.cantaloupe.player.Player;

/**
 * A virtual source synced across one or more players.
 * 
 * @author Dylan Scheltens
 *
 */
public class SharedLocalSource extends LocalSource implements ITimableSource {
    private boolean started        = false;
    private long    startTime      = 0L;
    private long    pauseTime      = 0L;
    private int     totalPauseTime = 0;

    protected SharedLocalSource(String ID) {
        super(ID);
    }

    /**
     * Starts the source.
     */
    public void start() {
        if (!this.started) {
            this.started = true;
            this.startTime = System.currentTimeMillis();

            for (Player player : this.players) {
                AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

                if (wrapper.isConnected()) {
                    if (wrapper.isConnected()) {
                        wrapper.getConnection().sendPacket(S005PacketPlayBounds.of(this, this.getVolume(), 0, this.getElapsedTime(), this.getSound().getDuration()));
                        wrapper.getSourceSettings(this).setPlaying(true);
                    }
                }
            }
        }
    }

    /**
     * Stops the source.
     */
    public void stop() {
        if (this.started) {
            this.started = false;
            this.startTime = -1L;
            this.pauseTime = -1L;
            this.totalPauseTime = 0;

            for (Player player : this.players) {
                AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

                if (wrapper.isConnected()) {
                    if (wrapper.isConnected()) {
                        wrapper.getConnection().sendPacket(S006PacketStop.of(this));
                        wrapper.getSourceSettings(this).setPlaying(false);
                    }
                }
            }
        }
    }

    /**
     * Pauses the source.
     */
    public void pause() {
        if (this.started) {
            for (Player player : this.players) {
                AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

                if (wrapper.isConnected()) {
                    if (wrapper.isConnected()) {
                        wrapper.getConnection().sendPacket(S007PacketPause.of(this));
                        wrapper.getSourceSettings(this).setPaused(true);
                    }
                }
            }

            this.pauseTime = System.currentTimeMillis();
        }
    }

    /**
     * Resumes the source.
     */
    public void resume() {
        if (this.started) {
            for (Player player : this.players) {
                AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

                if (wrapper.isConnected()) {
                    if (wrapper.isConnected()) {
                        wrapper.getConnection().sendPacket(S008PacketResume.of(this));
                        wrapper.getSourceSettings(this).setPaused(false);
                    }
                }
            }

            this.totalPauseTime += (System.currentTimeMillis() - this.pauseTime);
            this.pauseTime = 0L;
        }
    }

    /**
     * Adds a player to the source.
     * 
     * @param player
     *            The player
     */
    public void addPlayer(Player player) {
        AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

        if (wrapper.isConnected()) {
            if (wrapper.isConnected()) {
                if (this.started) {
                    wrapper.getConnection().sendPacket(S005PacketPlayBounds.of(this, this.getVolume(), 0, this.getElapsedTime(), this.getSound().getDuration()));
                    wrapper.addSourceSettings(SourcePlayerSettings.of(this, 100, 0, true, false));
                } else {
                    wrapper.addSourceSettings(SourcePlayerSettings.of(this, 100, 0, false, false));
                }

                this.players.add(player);
            }
        }
    }

    /**
     * Removes a player from the source.
     * 
     * @param player
     *            The player
     */
    public void removePlayer(Player player) {
        AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

        if (wrapper.isConnected()) {
            if (wrapper.isConnected()) {
                if (this.started) {
                    wrapper.getConnection().sendPacket(S006PacketStop.of(this));
                }

                wrapper.removeSourceSettings(this);

                this.players.remove(player);
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

    /**
     * Checks if the source has started.
     * 
     * @return True if it has, false if not
     */
    public boolean hasStarted() {
        return this.started;
    }

    @Override
    public void setSound(Sound sound, boolean update) {
        super.setSound(sound, false);

        if (update) {
            if (this.started) {
                this.stop();
            }

            this.start();
        }
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public int getElapsedTime() {
        return (int) (System.currentTimeMillis() - this.getStartTime()) - this.totalPauseTime;
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }
}