package org.cantaloupe.audio.source;

import java.util.List;

import org.cantaloupe.audio.sound.Sound;
import org.cantaloupe.player.Player;

/**
 * An interface containing basic source methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface ISource {
    /**
     * Clears the source.
     */
    public void clear();

    /**
     * Removes a player without sending a packet to the client.
     * 
     * @param player
     *            The player to remove
     */
    public void removePlayerInt(Player player);

    /**
     * Sets the source's settings.
     * 
     * @param settings
     *            The settings to set
     */
    public void setSettings(SourceSettings settings);

    /**
     * Sets the source's sound.
     * 
     * @param sound
     *            The sound to set
     * @param update
     *            Whether the currently playing sound should be stopped and
     *            replaced with the new one
     */
    public void setSound(Sound sound, boolean update);

    /**
     * Sets the source's sound.
     * 
     * @param sound
     *            The sound to set
     */
    public void setSound(Sound sound);

    /**
     * Gets the ID of the source.
     * 
     * @return The ID
     */
    public String getID();

    /**
     * Gets the settings of the source.
     * 
     * @return The settings
     */
    public SourceSettings getSettings();

    /**
     * Gets the sound of the source.
     * 
     * @return The sound
     */
    public Sound getSound();

    /**
     * Gets a list of players which are able to hear this source.
     * 
     * @return The list of players
     */
    public List<Player> getPlayers();
}