package org.cantaloupe.scoreboard;

/**
 * An abstract class containing basic methods for entries.
 * 
 * @author Dylan Scheltens
 *
 */
public abstract class Entry {
    protected Objective objective = null;

    protected Entry() {

    }

    protected void setObjective(Objective objective) {
        this.objective = objective;
    }

    /**
     * Gets the text of the entry.
     * 
     * @return The text
     */
    public abstract String getText();
}