package org.cantaloupe.audio.sound;

/**
 * A class containing the details about a sound.
 * 
 * @author Dylan Scheltens
 *
 */
public class Sound {
    private final String ID;
    private final String path;
    private final int    duration;

    private Sound(String ID, String path, int duration) {
        this.ID = ID;
        this.path = path;
        this.duration = duration;
    }

    /**
     * Creates and returns a new sound.
     * 
     * @param ID
     *            The ID of the sound
     * @param path
     *            The path of the sound file
     * @param duration
     *            The duration of the sound
     * @return The sound
     */
    public static Sound of(String ID, String path, int duration) {
        return new Sound(ID, path, duration);
    }

    /**
     * Gets the ID of the sound.
     * 
     * @return The ID
     */
    public String getID() {
        return this.ID;
    }

    /**
     * Gets the path of the sound file.
     * 
     * @return The path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Gets the duration of the sound.
     * 
     * @return The duration
     */
    public int getDuration() {
        return this.duration;
    }
}