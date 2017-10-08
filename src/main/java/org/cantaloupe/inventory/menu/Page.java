package org.cantaloupe.inventory.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;

/**
 * A class used to create a page.
 * 
 * @author Dylan Scheltens
 *
 */
public class Page {
    private Player                  holder    = null;
    private final String            ID;
    private final Text              name;
    private final int               size;
    private final ArrayList<Button> buttons;

    private Menu                    menu      = null;
    private Inventory               inventory = null;

    private Page(String ID, Text name, int size) {
        this.ID = ID;
        this.name = name;
        this.size = size;
        this.buttons = new ArrayList<Button>();
    }

    /**
     * Creates and returns a new page.
     * 
     * @param ID
     *            The ID
     * @return The page
     */
    public static Page of(String ID) {
        return new Page(ID, Text.of("Page"), 54);
    }

    /**
     * Creates and returns a new page.
     * 
     * @param ID
     *            The ID
     * @param name
     *            The name
     * @return The page
     */
    public static Page of(String ID, Text name) {
        return new Page(ID, name, 54);
    }

    /**
     * Creates and returns a new page.
     * 
     * @param ID
     *            The ID
     * @param size
     *            The size
     * @return The page
     */
    public static Page of(String ID, int size) {
        return new Page(ID, Text.of("Page"), 54);
    }

    /**
     * Creates and returns a new page.
     * 
     * @param ID
     *            The ID
     * @param name
     *            The name
     * @param size
     *            The size
     * @return The page
     */
    public static Page of(String ID, Text name, int size) {
        return new Page(ID, name, 54);
    }

    /**
     * Moves a button to a new slot on the page.
     * 
     * @param button
     *            The button
     * @param newSlot
     *            The slot
     */
    public void moveButton(Button button, int newSlot) {
        button.setSlot(newSlot);
    }

    /**
     * Refreshes a button on the page.
     * 
     * @param button
     *            The button
     */
    public void refreshButton(Button button) {
        this.inventory.setItem(button.getSlot(), button.getIcon().toHandle());
    }

    /**
     * Adds a button to the page.
     * 
     * @param button
     *            The button
     * @return The page
     */
    public Page addButton(Button button) {
        button.setPage(this);
        this.buttons.add(button);

        return this;
    }

    /**
     * Removes a button from the page.
     * 
     * @param slot
     *            The slot
     * @return The page
     */
    public Page removeButton(int slot) {
        this.buttons.remove(slot);

        if (this.inventory != null) {
            this.inventory.setItem(slot, null);
        }

        return this;
    }

    /**
     * Removes a button from the page.
     * 
     * @param button
     *            The button
     * @return The page
     */
    public Page removeButton(Button button) {
        this.buttons.remove(button.getSlot());

        if (this.inventory != null) {
            this.inventory.setItem(button.getSlot(), null);
        }

        return this;
    }

    /**
     * Refreshes the page.
     */
    public void refresh() {
        this.clear();
        this.build();
    }

    /**
     * Clears the page.
     */
    public void clear() {
        this.inventory.clear();
    }

    protected Page build() {
        this.inventory = Bukkit.getServer().createInventory(this.holder.toHandle(), this.size, this.name.toLegacy());

        for (Button button : this.buttons) {
            this.inventory.setItem(button.getSlot(), button.getIcon().toHandle());
        }

        return this;
    }

    /**
     * Checks if the slot contains a button.
     * 
     * @param slot
     *            The slot
     * @return True if it does, false if not
     */
    public boolean isButton(int slot) {
        for (Button button : this.buttons) {
            if (button.getSlot() == slot) {
                return true;
            }
        }

        return false;
    }

    protected void setMenu(Menu menu) {
        this.menu = menu;
    }

    protected void setHolder(Player holder) {
        this.holder = holder;
    }

    /**
     * Gets the menu containing the page.
     * 
     * @return The menu
     */
    public Menu getMenu() {
        return this.menu;
    }

    /**
     * Gets the holder of the page.
     * 
     * @return The holder
     */
    public Player getHolder() {
        return this.holder;
    }

    /**
     * Gets the ID of the page.
     * 
     * @return The ID
     */
    public String getID() {
        return this.ID;
    }

    /**
     * Gets the name of the page.
     * 
     * @return The name
     */
    public Text getName() {
        return this.name;
    }

    /**
     * Gets the size of the page.
     * 
     * @return The size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Gets a button from the page.
     * 
     * @param slot
     *            The slot of the button
     * @return The button
     */
    public Button getButton(int slot) {
        for (Button button : this.buttons) {
            if (button.getSlot() == slot) {
                return button;
            }
        }

        return null;
    }

    /**
     * Gets a list of buttons on the page.
     * 
     * @return The list of buttons
     */
    public List<Button> getButtons() {
        return this.buttons;
    }

    protected Inventory getInventory() {
        return this.inventory;
    }
}