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
    private int     length = 0;
    private boolean dotted = false;

    private LineEntry(int length, boolean dotted) {
        super();

        this.length = length;
        this.dotted = dotted;
    }

    /**
     * Creates and returns a new entry.
     * 
     * @param text
     *            The text
     * @param dotted
     *            Whether the line is dotted or not
     * 
     * @return The entry
     */
    public static LineEntry of(int length, boolean dotted) {
        return new LineEntry(length, dotted);
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
        return new LineEntry(length, false);
    }

    @Override
    public String getText() {
        String string = "";

        for (int i = 0; i < this.objective.getLineCount(); i++) {
            string += " ";
        }
        
        return Text.fromLegacy((!this.dotted ? "&m" : "") + "+------" + this.getDashes(this.length) + "------+&r" + string).toLegacy();
    }

    private String getDashes(int length) {
        String dashes = "";

        for (int i = 0; i < length - 8; i++) {
            dashes += "-";
        }

        return dashes;
    }
}