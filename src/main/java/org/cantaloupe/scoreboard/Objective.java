package org.cantaloupe.scoreboard;

import org.bukkit.scoreboard.Score;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.scoreboard.entry.LineEntry;
import org.cantaloupe.scoreboard.entry.SpaceEntry;
import org.cantaloupe.text.Text;

/**
 * A class used to create a scoreboard objective.
 * 
 * @author Dylan Scheltens
 *
 */
public class Objective {
    private final org.bukkit.scoreboard.Objective handle;
    private final DataContainer<Integer, Entry>   entries;
    private int                                   spaceCount = 0;
    private int                                   lineCount  = 0;

    protected Objective(org.bukkit.scoreboard.Objective handle) {
        this.handle = handle;
        this.entries = DataContainer.of();
    }

    /**
     * Removes an entry from this objective.
     * 
     * @param index
     *            The index of the entry
     */
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

    /**
     * Removes an entry from this objective.
     * 
     * @param index
     *            The index of the entry
     */
    public void clearEntry(int index) {
        if (this.entries.containsKey(index)) {
            this.handle.getScoreboard().resetScores(this.entries.get(index).getText());
        }
    }

    /**
     * Adds an entry to the objective.
     * 
     * @param index
     *            The index of the entry
     * @param entry
     *            The entry
     */
    public void addEntry(int index, Entry entry) {
        if (this.entries.containsKey(index)) {
            this.handle.getScoreboard().resetScores(this.entries.get(index).getText());
            this.entries.remove(index);
        }

        entry.setObjective(this);

        if (entry instanceof SpaceEntry) {
            this.spaceCount++;
        } else if (entry instanceof LineEntry) {
            this.lineCount++;
        }

        Score score = this.handle.getScore(entry.getText());
        score.setScore(15 - index);

        this.entries.put(index, entry);
    }

    /**
     * Sets the title of the objective.
     * 
     * @param title
     *            The title
     */
    public void setTitle(Text title) {
        this.handle.setDisplayName(title.toLegacy());
    }

    /**
     * Sets the display slot of the objective.
     * 
     * @param slot
     *            The slot
     */
    public void setSlot(DisplaySlot slot) {
        this.handle.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.valueOf(slot.name()));
    }

    /**
     * Returns the handle of the objective.
     * 
     * @return The handle
     */
    public org.bukkit.scoreboard.Objective toHandle() {
        return this.handle;
    }

    /**
     * Gets an entry from the objective.
     * 
     * @param index
     *            The index of the entry
     * @return The entry
     */
    public Entry getEntry(int index) {
        return this.entries.get(index);
    }

    /**
     * Gets the name of the objective.
     * 
     * @return The name
     */
    public String getName() {
        return this.handle.getName();
    }

    /**
     * Gets the title of the objective.
     * 
     * @return The title
     */
    public Text getTitle() {
        return Text.fromLegacy(this.handle.getDisplayName());
    }

    protected DisplaySlot getSlot() {
        return DisplaySlot.valueOf(this.handle.getDisplaySlot().name());
    }

    /**
     * Gets the space count of the objective.
     * 
     * @return The space count
     */
    public int getSpaceCount() {
        return this.spaceCount;
    }

    /**
     * Gets the line count of the objective.
     * 
     * @return The line count
     */
    public int getLineCount() {
        return this.lineCount;
    }

    /**
     * Gets a container of entries from the objective.
     * 
     * @return The collection of entries
     */
    public DataContainer<Integer, Entry> getEntries() {
        return this.entries.clone();
    }

    /**
     * An enum containing the possible display slots of an objective.
     * 
     * @author Dylan Scheltens
     * 
     */
    public static enum DisplaySlot {
        BELOW_NAME, PLAYER_LIST, SIDEBAR
    }
}