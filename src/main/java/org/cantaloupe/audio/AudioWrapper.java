package org.cantaloupe.audio;

import java.util.Collection;
import java.util.UUID;

import org.cantaloupe.audio.source.ISource;
import org.cantaloupe.audio.source.SourcePlayerSettings;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.network.web.server.WebServerConnection;
import org.cantaloupe.player.Player;
import org.cantaloupe.player.PlayerWrapper;

/**
 * A player wrapper containing methods for the audio server.
 * 
 * @author Dylan Scheltens
 *
 */
public class AudioWrapper extends PlayerWrapper {
    private WebServerConnection                         connection     = null;
    private UUID                                        cid            = null;
    private DataContainer<String, SourcePlayerSettings> sourceSettings = null;

    public AudioWrapper(Player player) {
        super(player);

        this.sourceSettings = DataContainer.of();
    }

    @Override
    public void onUnload() {
        if (this.isConnected()) {
            this.connection.closeExt();
        }

        this.clearSourceSettings();
    }

    /**
     * Clears all the source settings of the {@link org.cantaloupe.player.Player
     * player}.
     */
    public void clearSourceSettings() {
        for (SourcePlayerSettings srcSetting : this.sourceSettings.valueSet()) {
            srcSetting.getSource().removePlayerInt(this.getPlayer());
        }

        this.sourceSettings.clear();
    }

    /**
     * Checks if the {@link org.cantaloupe.player.Player player} has the
     * settings for a source.
     * 
     * @param source
     *            The source
     * @return True if the player does, false if not
     */
    public boolean hasSourceSettings(ISource source) {
        return this.hasSourceSettings(source.getID());
    }

    /**
     * Checks if the player has the settings for a source.
     * 
     * @param sourceID
     *            The ID of a source
     * @return True if the player does, false if not
     */
    public boolean hasSourceSettings(String sourceID) {
        return this.sourceSettings.containsKey(sourceID);
    }

    /**
     * Adds the settings of a source to the {@link org.cantaloupe.player.Player
     * player}.
     * 
     * @param settings
     *            The settings to add
     */
    public void addSourceSettings(SourcePlayerSettings settings) {
        this.sourceSettings.put(settings.getSource().getID(), settings);
    }

    /**
     * Removes the source's settings from the player
     * {@link org.cantaloupe.player.Player player}.
     * 
     * @param source
     *            The source
     */
    public void removeSourceSettings(ISource source) {
        this.removeSourceSettings(source.getID());
    }

    /**
     * Removes the source's settings from the player
     * {@link org.cantaloupe.player.Player player}.
     * 
     * @param sourceID
     *            The ID of a source
     */
    public void removeSourceSettings(String sourceID) {
        this.sourceSettings.remove(sourceID);
    }

    /**
     * Generates an URL containing the {@link org.cantaloupe.player.Player
     * player}'s UUID and newly generated CID.
     * 
     * @param host
     *            The host of the audio server
     * @return The URL
     */
    public String generateConnectURL(String host) {
        this.cid = UUID.randomUUID();

        return host + "?cid=" + this.cid.toString() + "&uuid=" + this.getPlayer().getUUID().toString();
    }

    /**
     * Disconnects the {@link org.cantaloupe.player.Player player} from the
     * audio server.
     */
    public void disconnect() {
        this.cid = null;
        this.connection = null;
    }

    /**
     * Checks if the {@link org.cantaloupe.player.Player player} is connected to
     * the audio server.
     * 
     * @return True if connected, false if not
     */
    public boolean isConnected() {
        return this.connection != null;
    }

    /**
     * Sets the connection of the {@link org.cantaloupe.player.Player player}.
     * 
     * @param connection
     *            The connection to set
     */
    public void setConnection(WebServerConnection connection) {
        this.connection = connection;
    }

    /**
     * Gets the source settings of a source.
     * 
     * @param source
     *            The source
     * @return The source settings
     */
    public SourcePlayerSettings getSourceSettings(ISource source) {
        return this.sourceSettings.get(source.getID());
    }

    /**
     * Gets the source settings of a source.
     * 
     * @param sourceID
     *            The ID of a source
     * @return The source settings
     */
    public SourcePlayerSettings getSourceSettings(String sourceID) {
        return this.sourceSettings.get(sourceID);
    }

    /**
     * Gets all the settings of the {@link org.cantaloupe.player.Player player}.
     * 
     * @return A collection of all settings
     */
    public Collection<SourcePlayerSettings> getSourceSettings() {
        return this.sourceSettings.valueSet();
    }

    /**
     * Gets the connection of the {@link org.cantaloupe.player.Player player}.
     * 
     * @return The connection
     */
    public WebServerConnection getConnection() {
        return this.connection;
    }

    /**
     * Gets the CID of the {@link org.cantaloupe.player.Player player}.
     * 
     * @return The CID
     */
    public UUID getCID() {
        return this.cid;
    }
}