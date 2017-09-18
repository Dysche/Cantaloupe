package org.cantaloupe.audio.source;

/**
 * A class containing a player's settings for a source.
 * 
 * @author Dylan Scheltens
 *
 */
public class SourcePlayerSettings {
    private ISource source  = null;
    private int     volume  = 0;
    private int     pan     = 0;
    private boolean playing = false;
    private boolean paused  = false;

    private SourcePlayerSettings(ISource source, int volume, int pan, boolean playing, boolean paused) {
        this.source = source;
        this.volume = volume;
        this.pan = pan;
        this.playing = playing;
        this.paused = paused;
    }

    /**
     * Creates and returns a new instance of this class
     * 
     * @param source
     *            The source
     * @param volume
     *            The volume
     * @param pan
     *            The pan
     * @param playing
     *            Whether or not the source is playing
     * @param paused
     *            Whether or not the source is paused
     * @return The instance
     */
    public static SourcePlayerSettings of(ISource source, int volume, int pan, boolean playing, boolean paused) {
        return new SourcePlayerSettings(source, volume, pan, playing, paused);
    }

    /**
     * Checks if the source is currently playing.
     * 
     * @return True if it is, false if not
     */
    public boolean isPlaying() {
        return this.playing;
    }

    /**
     * Checks if the source is currently paused.
     * 
     * @return True if it is, false if not
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * Sets the volume.
     * 
     * @param volume
     *            The volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     * Sets the pan.
     * 
     * @param pan
     *            The pan
     */
    public void setPan(int pan) {
        this.pan = pan;
    }

    /**
     * Sets whether or not the source is currently playing.
     * 
     * @param playing
     *            Whether or not the source is playing
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * Sets whether or not the source is currently paused.
     * 
     * @param paused
     *            Whether or not the source is paused
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
        this.playing = !paused;
    }

    /**
     * Gets the source.
     * 
     * @return The source
     */
    public ISource getSource() {
        return this.source;
    }

    /**
     * Gets the volume.
     * 
     * @return The volume
     */
    public int getVolume() {
        return this.volume;
    }

    /**
     * Gets the pan.
     * 
     * @return The pan
     */
    public int getPan() {
        return this.pan;
    }
}