package org.cantaloupe.scoreboard.entry;

import org.cantaloupe.scoreboard.Entry;
import org.cantaloupe.text.Text;

public class CenteredTextEntry extends Entry {
    private Text text    = null;
    private int  maxSize = 0;

    private CenteredTextEntry(Text text, int maxSize) {
        super();

        this.text = Text.of(this.getSpacesForCentering(maxSize, text.toLegacy())).append(text);
        this.maxSize = maxSize;
    }

    public static CenteredTextEntry of(Text text, int maxSize) {
        return new CenteredTextEntry(text, maxSize);
    }

    public void setText(Text text) {
        if (this.objective != null) {
            int index = -1;

            for (int i : this.objective.getEntries().keySet()) {
                if (this.objective.getEntry(i) == this) {
                    index = i;
                }
            }

            if (index != -1) {
                this.objective.removeEntry(index);
                this.text = Text.of(this.getSpacesForCentering(this.maxSize, text.toLegacy())).append(text);
                this.objective.addEntry(index, this);
            }
        } else {
            this.text = Text.of(this.getSpacesForCentering(this.maxSize, text.toLegacy())).append(text);
        }
    }

    private String getSpacesForCentering(int maxLength, String string) {
        int length = string.length();
        
        if(length % 2 == 0) {
            length += 1;
        }
        
        int diff = maxLength - length;
        int amount = diff / 2;
        String toReturn = "";

        for (int i = 0; i < amount; i++) {
            toReturn += " ";
        }

        return toReturn;
    }

    @Override
    public String getText() {
        return this.text.toLegacy();
    }
}