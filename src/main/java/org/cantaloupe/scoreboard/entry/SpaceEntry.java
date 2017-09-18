package org.cantaloupe.scoreboard.entry;

import org.cantaloupe.scoreboard.Entry;

/**
 * A class used to create a whitespace on a scoreboard.
 * 
 * @author Dylan Scheltens
 *
 */
public class SpaceEntry extends Entry {
    private SpaceEntry() {
        super();
    }

    /**
     * Creates and returns a new entry.
     * 
     * @return The entry
     */
    public static SpaceEntry of() {
        return new SpaceEntry();
    }

    @Override
    public String getText() {
        String string = "";

        for (int i = 0; i < this.objective.getSpaceCount(); i++) {
            string += " ";
        }

        return string;
    }
}