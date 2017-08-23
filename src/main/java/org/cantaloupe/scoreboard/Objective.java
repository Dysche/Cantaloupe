package org.cantaloupe.scoreboard;

import org.bukkit.scoreboard.Score;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.scoreboard.entry.SpaceEntry;
import org.cantaloupe.text.Text;

public class Objective {
    private final org.bukkit.scoreboard.Objective handle;
    private final DataContainer<Integer, Entry>   entries;
    private int                                   spaceCount = 0;

    protected Objective(org.bukkit.scoreboard.Objective handle) {
        this.handle = handle;
        this.entries = DataContainer.of();
    }
    
    public void removeEntry(int index) {
        if (this.entries.containsKey(index)) {
            Entry entry = this.entries.get(index);
            entry.setObjective(null);

            if (entry instanceof SpaceEntry) {
                this.spaceCount--;
            }

            this.handle.getScoreboard().resetScores(entry.getText());
            this.entries.remove(index);
        }
    }
    
    public void clearEntry(int index) {
        if (this.entries.containsKey(index)) {
            this.handle.getScoreboard().resetScores(this.entries.get(index).getText());
        }
    }

    public org.bukkit.scoreboard.Objective toHandle() {
        return this.handle;
    }

    public void addEntry(int index, Entry entry) {
        if (this.entries.containsKey(index)) {
            this.handle.getScoreboard().resetScores(this.entries.get(index).getText());
            this.entries.remove(index);
        }

        entry.setObjective(this);

        if (entry instanceof SpaceEntry) {
            this.spaceCount++;
        }

        Score score = this.handle.getScore(entry.getText());
        score.setScore(15 - index);

        this.entries.put(index, entry);
    }

    public void setTitle(Text title) {
        this.handle.setDisplayName(title.toLegacy());
    }

    public void setSlot(Slot slot) {
        this.handle.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.valueOf(slot.name()));
    }

    public Entry getEntry(int index) {
        return this.entries.get(index);
    }

    public String getName() {
        return this.handle.getName();
    }

    public Text getTitle() {
        return Text.fromLegacy(this.handle.getDisplayName());
    }

    protected Slot getSlot() {
        return Slot.valueOf(this.handle.getDisplaySlot().name());
    }

    public int getSpaceCount() {
        return this.spaceCount;
    }
    
    public DataContainer<Integer, Entry> getEntries() {
        return this.entries.clone();
    }

    public static enum Slot {
        BELOW_NAME, PLAYER_LIST, SIDEBAR
    }
}