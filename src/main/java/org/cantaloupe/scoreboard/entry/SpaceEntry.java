package org.cantaloupe.scoreboard.entry;

import org.cantaloupe.scoreboard.Entry;

public class SpaceEntry extends Entry {
    private SpaceEntry() {
        super();
    }

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