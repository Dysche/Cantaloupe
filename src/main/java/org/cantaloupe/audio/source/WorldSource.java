package org.cantaloupe.audio.source;

import java.util.List;
import java.util.Optional;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.audio.AudioServer;
import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.audio.network.packets.S004PacketPlay;
import org.cantaloupe.audio.network.packets.S005PacketPlayBounds;
import org.cantaloupe.audio.network.packets.S006PacketStop;
import org.cantaloupe.audio.network.packets.S007PacketPause;
import org.cantaloupe.audio.network.packets.S008PacketResume;
import org.cantaloupe.audio.network.packets.S009PacketVolume;
import org.cantaloupe.audio.network.packets.S010PacketPan;
import org.cantaloupe.audio.network.packets.S011PacketVolumePan;
import org.cantaloupe.audio.sound.Sound;
import org.cantaloupe.player.Player;
import org.cantaloupe.util.DataUtils;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;

/**
 * An abstract class containing methods for physical sources.
 * 
 * @author Dylan Scheltens
 *
 */
public abstract class WorldSource extends WorldObject implements ISource, ITimableSource {
    private final String         ID;
    private ImmutableLocation    location       = null;

    private SourceSettings       settings       = null;
    private Sound                sound          = null;
    private boolean              started        = false;
    private long                 startTime      = 0L;
    private long                 pauseTime      = 0L;
    private int                  totalPauseTime = 0;

    protected final List<Player> players;

    protected WorldSource(String ID, ImmutableLocation location) {
        this.ID = ID;
        this.location = location;

        this.players = DataUtils.newArrayList();
    }

    @Override
    protected void onPlaced() {
        Optional<AudioServer> audioServerOpt = Cantaloupe.getAudioServer();

        if (audioServerOpt.isPresent()) {
            audioServerOpt.get().getSourceManager().addSource(this);
        }
    }

    @Override
    protected void onRemoved() {
        Optional<AudioServer> audioServerOpt = Cantaloupe.getAudioServer();

        if (audioServerOpt.isPresent()) {
            audioServerOpt.get().getSourceManager().removeSourceInt(this);
        }
    }

    /**
     * Starts the source.
     */
    public void start() {
        if (!this.started) {
            this.started = true;
            this.startTime = System.currentTimeMillis();
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
                this.stop(player);
            }
        }
    }

    /**
     * Pauses the source.
     */
    public void pause() {
        if (this.started) {
            for (Player player : this.players) {
                if (player.hasWrapper(AudioWrapper.class)) {
                    AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

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
                if (player.hasWrapper(AudioWrapper.class)) {
                    AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

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

    @Override
    public void clear() {
        for (Player player : this.players) {
            if (player.hasWrapper(AudioWrapper.class)) {
                player.getWrapper(AudioWrapper.class).removeSourceSettings(this);
            }
        }

        this.players.clear();
    }

    protected void play(Player player, int volume, int pan) {
        if (player.hasWrapper(AudioWrapper.class)) {
            AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);
            wrapper.getConnection().sendPacket(S004PacketPlay.of(this, volume, pan));
            wrapper.getSourceSettings(this).setPlaying(true);
        }
    }

    protected void playBounds(Player player, int volume, int pan, int begin, int end) {
        if (player.hasWrapper(AudioWrapper.class)) {
            AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);
            wrapper.getConnection().sendPacket(S005PacketPlayBounds.of(this, volume, pan, begin, end));
            wrapper.getSourceSettings(this).setPlaying(true);
        }
    }

    protected void stop(Player player) {
        if (player.hasWrapper(AudioWrapper.class)) {
            AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);
            wrapper.getConnection().sendPacket(S006PacketStop.of(this));
            wrapper.getSourceSettings(this).setPlaying(false);
        }
    }

    protected void setVolume(Player player, int volume) {
        if (player.hasWrapper(AudioWrapper.class)) {
            AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);
            wrapper.getConnection().sendPacket(S009PacketVolume.of(this, volume));
            wrapper.getSourceSettings(this).setVolume(volume);
        }
    }

    protected void setPan(Player player, int pan) {
        if (player.hasWrapper(AudioWrapper.class)) {
            AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);
            wrapper.getConnection().sendPacket(S010PacketPan.of(this, pan));
            wrapper.getSourceSettings(this).setPan(pan);
        }
    }

    protected void setVolumePan(Player player, int volume, int pan) {
        if (player.hasWrapper(AudioWrapper.class)) {
            AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);
            wrapper.getConnection().sendPacket(S011PacketVolumePan.of(this, volume, pan));
            wrapper.getSourceSettings(this).setVolume(volume);
            wrapper.getSourceSettings(this).setPan(pan);
        }
    }

    protected void addPlayer(Player player) {
        if (player.hasWrapper(AudioWrapper.class)) {
            player.getWrapper(AudioWrapper.class).addSourceSettings(SourcePlayerSettings.of(this, 0, 0, false, false));
        }

        this.players.add(player);
    }

    protected void removePlayer(Player player) {
        if (player.hasWrapper(AudioWrapper.class)) {
            player.<AudioWrapper>getWrapper(AudioWrapper.class).removeSourceSettings(this);
        }

        this.players.remove(player);
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
    public void setSettings(SourceSettings settings) {
        this.settings = settings;
    }

    @Override
    public void setSound(Sound sound, boolean update) {
        this.sound = sound;

        if (update) {
            if (this.started) {
                this.stop();
                this.start();
            }
        }
    }

    @Override
    public void setSound(Sound sound) {
        this.setSound(sound, false);
    }

    @Override
    public String getID() {
        return this.ID;
    }

    @Override
    public ImmutableLocation getLocation() {
        return this.location;
    }

    @Override
    public SourceSettings getSettings() {
        return this.settings;
    }

    @Override
    public Sound getSound() {
        return this.sound;
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