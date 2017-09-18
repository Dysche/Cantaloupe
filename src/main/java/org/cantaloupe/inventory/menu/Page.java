package org.cantaloupe.inventory.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.cantaloupe.player.Player;

/**
 * A class used to create a page.
 * 
 * @author Dylan Scheltens
 *
 */
public class Page {
    private Player            holder    = null;
    private String            ID        = null;
    private String            name      = "Page";
    private int               size      = 54;
    private ArrayList<Button> buttons   = null;

    private Menu              menu      = null;
    private Inventory         inventory = null;

    private Page() {
        this.buttons = new ArrayList<Button>();
    }

    /**
     * Creates and returns a new page.
     * 
     * @return The page
     */
    public static Page of() {
        return new Page();
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
     */
    public void addButton(Button button) {
        button.setPage(this);
        this.buttons.add(button);
    }

    /**
     * Removes a button from the page.
     * 
     * @param slot
     *            The slot
     */
    public void removeButton(int slot) {
        this.buttons.remove(slot);

        if (this.inventory != null) {
            this.inventory.setItem(slot, null);
        }
    }

    /**
     * Removes a button from the page.
     * 
     * @param button
     *            The button
     */
    public void removeButton(Button button) {
        this.buttons.remove(button.getSlot());

        if (this.inventory != null) {
            this.inventory.setItem(button.getSlot(), null);
        }
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
        this.inventory = Bukkit.getServer().createInventory(this.holder.toHandle(), this.size, this.name);

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

    /**
     * Sets the ID of the page.
     * 
     * @param ID
     *            The ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Sets the name of the name.
     * 
     * @param name
     *            The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the size of the name.
     * 
     * @param size
     *            The size
     */
    public void setSize(int size) {
        this.size = size;
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
    public String getName() {
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