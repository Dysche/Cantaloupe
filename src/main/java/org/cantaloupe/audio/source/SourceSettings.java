package org.cantaloupe.audio.source;

import org.cantaloupe.player.Player;
import org.joml.Vector3d;

/**
 * A class containing the settings for a source.
 * 
 * @author Dylan Scheltens
 *
 */
public class SourceSettings {
    private boolean         usePan;
    private boolean         useAttenuation;
    private double          minRadius;
    private double          maxRadius;
    private AttenuationType attenuationType;

    private SourceSettings(boolean usePan, boolean useAttenuation, double minRadius, double maxRadius, AttenuationType attenuationType) {
        this.usePan = usePan;
        this.useAttenuation = useAttenuation;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.attenuationType = attenuationType;
    }

    /**
     * Creates and returns a new instance of this class with panning and
     * attenuation disabled.
     * 
     * @return The instance
     */
    public static SourceSettings of() {
        return new SourceSettings(false, false, 0.0, 0.0, null);
    }

    /**
     * Creates and returns a new instance of this class with no attenuation.
     * 
     * @param usePan
     *            Whether or not to use panning
     * @return The instance
     */
    public static SourceSettings of(boolean usePan) {
        return new SourceSettings(usePan, false, 0.0, 0.0, null);
    }

    /**
     * Creates and returns a new instance of this class with panning disabled
     * and attenuation enabled.
     * 
     * @param minRadius
     *            The minimal radius of the attenuation
     * @param maxRadius
     *            The maximum radius of the attenuation
     * @param attenuationType
     *            The type of the attenuation
     * @return The instance
     */
    public static SourceSettings of(double minRadius, double maxRadius, AttenuationType attenuationType) {
        return new SourceSettings(false, true, minRadius, maxRadius, attenuationType);
    }

    /**
     * Creates and returns a new instance of this class with attenuation
     * enabled.
     * 
     * @param usePan
     *            Whether or not to use panning
     * @param minRadius
     *            The minimal radius of the attenuation
     * @param maxRadius
     *            The maximum radius of the attenuation
     * @param attenuationType
     *            The type of the attenuation
     * @return The instance
     */
    public static SourceSettings of(boolean usePan, double minRadius, double maxRadius, AttenuationType attenuationType) {
        return new SourceSettings(usePan, true, minRadius, maxRadius, attenuationType);
    }

    /**
     * Creates and returns a new instance of this class.
     * 
     * @param usePan
     *            Whether or not to use panning
     * @param useAttenuation
     *            Whether or not to use attenuation
     * @param minRadius
     *            The minimal radius of the attenuation
     * @param maxRadius
     *            The maximum radius of the attenuation
     * @param attenuationType
     *            The type of the attenuation
     * @return The instance
     */
    public static SourceSettings of(boolean usePan, boolean useAttenuation, double minRadius, double maxRadius, AttenuationType attenuationType) {
        return new SourceSettings(usePan, useAttenuation, minRadius, maxRadius, attenuationType);
    }

    /**
     * Calculates the attenuation for the player.
     * 
     * @param source
     *            The source
     * @param player
     *            The player
     * @return The volume
     */
    public int calculateVolume(WorldSource source, Player player) {
        double distance = player.getLocation().distance(source.getLocation());
        double volume = 0;

        if (distance <= this.minRadius) {
            return 100;
        }
        if (distance > this.maxRadius) {
            return 0;
        }

        switch (this.attenuationType) {
            case LOGARITHMIC:
                // volume = 100D - (Math.abs(Math.sin(0.032D * (distance -
                // this.minRadius))) + 100 * Math.abs(Math.sin((0.016D /
                // (distance / this.maxRadius)) * (distance -
                // this.minRadius))));
                // volume = 100D - (Math.log((distance - this.minRadius) /
                // (distance / this.maxRadius)) * 50D);

                break;

            case LINEAR:
                double min = 0D;
                double max = 100D;
                double distanceAlpha = (distance - this.minRadius) / this.maxRadius;

                volume = 100D - ((min * (1D - distanceAlpha)) + (max * distanceAlpha));

                break;
        }

        return (int) volume;
    }

    /**
     * Calculates the panning for the player.
     * 
     * @param source
     *            The source
     * @param player
     *            The player
     * @return The pan
     */
    public int calculatePan(WorldSource source, Player player) {
        Vector3d up = new Vector3d(0D, 1D, 0D);
        Vector3d lookAt = player.getLocation().getDirection();
        Vector3d side = up.cross(lookAt);

        return (int) (-Math.sin(Math.atan2(source.getLocation().getPosition().sub(player.getPosition()).dot(side), source.getLocation().getPosition().sub(player.getPosition()).dot(lookAt))) * 100);
    }

    /**
     * Checks if attenuation is enabled.
     * 
     * @return True if enabled, false if not
     */
    public boolean useAttenuation() {
        return this.useAttenuation;
    }

    /**
     * Checks if panning is enabled.
     * 
     * @return True if enabled, false if not
     */
    public boolean usePan() {
        return this.usePan;
    }

    /**
     * Sets whether or not attenuation is enabled.
     * 
     * @param useAttenuation
     *            Whether or not attenuation is enabled
     */
    public void setUseAttenuation(boolean useAttenuation) {
        this.useAttenuation = useAttenuation;
    }

    /**
     * Sets whether or not panning is enabled.
     * 
     * @param usePan
     *            Whether or not panning is enabled
     */
    public void setUsePan(boolean usePan) {
        this.usePan = usePan;
    }

    /**
     * Gets the minimal radius of the attenuation.
     * 
     * @return The minimal radius
     */
    public double getMinRadius() {
        return this.minRadius;
    }

    /**
     * Gets the maximum radius of the attenuation.
     * 
     * @return The maximum radius
     */
    public double getMaxRadius() {
        return this.maxRadius;
    }

    public enum AttenuationType {
        LINEAR, LOGARITHMIC;
    }
}