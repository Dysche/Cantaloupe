package org.cantaloupe.scoreboard.entry;

import org.cantaloupe.scoreboard.Entry;
import org.cantaloupe.text.Text;

/**
 * A class used to create a whitespace on a scoreboard.
 * 
 * @author Dylan Scheltens
 *
 */
public class LineEntry extends Entry {
    private int length = 0;

    private LineEntry(int length) {
        super();

        this.length = length;
    }

    /**
     * Creates and returns a new entry.
     * 
     * @param text
     *            The text
     * 
     * @return The entry
     */
    public static LineEntry of(int length) {
        return new LineEntry(length);
    }

    @Override
    public String getText() {
        return Text.fromLegacy("&m+------" + this.getDashes(this.length) + "------+").toLegacy();
    }

    private String getDashes(int length) {
        String dashes = "";

        for (int i = 0; i < length - 8; i++) {
            dashes += "-";
        }

        return dashes;
    }
}