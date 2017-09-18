package org.cantaloupe.audio.source;

/**
 * An interface used to make a source time-able.
 * 
 * @author Dylan Scheltens
 *
 */
public interface ITimableSource {
    /**
     * Gets the start time of the source in milliseconds.
     * 
     * @return The start time
     */
    public long getStartTime();

    /**
     * Gets the amount of time since the source started in milliseconds.
     * 
     * @return The elapsed time
     */
    public int getElapsedTime();
}
