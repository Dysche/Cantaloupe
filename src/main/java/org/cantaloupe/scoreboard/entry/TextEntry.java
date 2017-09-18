package org.cantaloupe.scoreboard.entry;

import org.cantaloupe.scoreboard.Entry;
import org.cantaloupe.text.Text;

/**
 * A class used to create a whitespace on a scoreboard.
 * 
 * @author Dylan Scheltens
 *
 */
public class TextEntry extends Entry {
    private Text text = null;

    private TextEntry(Text text) {
        super();

        this.text = text;
    }

    /**
     * Creates and returns a new entry.
     * 
     * @param text
     *            The text
     * 
     * @return The entry
     */
    public static TextEntry of(Text text) {
        return new TextEntry(text);
    }

    /**
     * Sets the text of the entry.
     * 
     * @param text
     *            The text
     */
    public void setText(Text text) {
        if (this.objective != null) {
            int index = -1;

            for (int i : this.objective.getEntries().keySet()) {
                if (this.objective.getEntry(i) == this) {
                    index = i;
                }
            }

            if (index != -1) {
                this.objective.clearEntry(index);
                this.text = text;
                this.objective.addEntry(index, this);
            }
        } else {
            this.text = text;
        }
    }

    @Override
    public String getText() {
        return this.text.toLegacy();
    }
}