package org.cantaloupe.audio.sound;

import java.util.Collection;
import java.util.Optional;

import org.cantaloupe.data.DataContainer;

/**
 * A class used to manage sounds.
 * 
 * @author Dylan Scheltens
 *
 */
public class SoundManager {
    private final DataContainer<String, Sound> sounds;

    private SoundManager() {
        this.sounds = DataContainer.of();
    }

    /**
     * Creates and returns a new sound manager.
     * 
     * @return The sound manager
     */
    public static SoundManager of() {
        return new SoundManager();
    }

    /**
     * Creates and adds a sound to the sound manager.
     * 
     * @param ID
     *            The ID of the sound
     * @param path
     *            The path of the sound
     * @param duration
     *            The duration of the sound
     */
    public void addSound(String ID, String path, int duration) {
        this.sounds.put(ID, Sound.of(ID, path, duration));
    }

    /**
     * Adds a sound to the sound manager.
     * 
     * @param sound
     *            The sound
     */
    public void addSound(Sound sound) {
        this.sounds.put(sound.getID(), sound);
    }

    /**
     * Removes a sound from the sound manager.
     * 
     * @param sound
     *            The sound
     */
    public void removeSound(Sound sound) {
        this.removeSound(sound.getID());
    }

    /**
     * Removes a sound from the sound manager.
     * 
     * @param soundID
     *            The ID of a sound
     */
    public void removeSound(String soundID) {
        this.sounds.remove(soundID);
    }

    /**
     * Checks if a sound is present in the sound manager.
     * 
     * @param soundID
     *            The ID of a sound
     * @return True if it's present, false if not
     */
    public boolean hasSound(String soundID) {
        return this.sounds.containsKey(soundID);
    }

    /**
     * Gets a sound from the sound manager.
     * 
     * @param soundID
     *            The ID of a sound
     * @return An optional containing the sound if it's present, an empty
     *         optional if not
     */
    public Optional<Sound> getSound(String soundID) {
        return Optional.ofNullable(this.sounds.get(soundID));
    }

    /**
     * Gets a collection of sounds from the sound manager.
     * 
     * @return The collection of sounds
     */
    public Collection<Sound> getSounds() {
        return this.sounds.valueSet();
    }
}