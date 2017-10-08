package org.cantaloupe.inventory.menu;

import java.util.Collection;
import java.util.HashMap;

import org.cantaloupe.player.Player;

/**
 * A class used to create a menu.
 * 
 * @author Dylan Scheltens
 *
 */
public class Menu {
    private Player                      holder      = null;
    private final HashMap<String, Page> pages;

    private Page                        currentPage = null;
    private boolean                     isDirty     = false;

    private Menu(Player holder) {
        this.holder = holder;
        this.pages = new HashMap<String, Page>();
    }

    /**
     * Creates and returns a new menu.
     * 
     * @param holder
     *            The holder
     * @return The menu
     */
    public static Menu of(Player holder) {
        return new Menu(holder);
    }

    /**
     * Opens the menu.
     */
    public void open() {
        if (this.getPage("landing") != null) {
            this.showLandingPage();
        }
    }

    /**
     * Closes the menu.
     */
    public void close() {
        this.holder.closeMenu();
    }

    /**
     * Shows the landing page of the menu.
     */
    public void showLandingPage() {
        this.isDirty = true;

        this.holder.toHandle().openInventory(this.pages.get("landing").getInventory());
        this.currentPage = this.pages.get("landing");

        this.isDirty = false;
    }

    /**
     * Shows a page from the menu.
     * 
     * @param ID
     *            The ID of a page
     */
    public void showPage(String ID) {
        if (this.pages.get(ID) != null) {
            this.isDirty = true;

            this.holder.toHandle().openInventory(this.pages.get(ID).getInventory());
            this.currentPage = this.pages.get(ID);

            this.isDirty = false;
        }
    }

    /**
     * Adds a page to the menu.
     * 
     * @param page
     *            The page
     */
    public void addPage(Page page) {
        page.setMenu(this);
        page.setHolder(this.holder);
        page.build();

        this.pages.put(page.getID(), page);
    }

    /**
     * Removes a page from the menu.
     * 
     * @param ID
     *            The ID of a page
     */
    public void removePage(String ID) {
        if (this.pages.containsKey(ID)) {
            this.pages.get(ID).clear();
            this.pages.remove(ID);
        }
    }

    /**
     * Sets the landing page of the menu.
     * 
     * @param page
     *            The landing page
     */
    public void setLandingPage(Page page) {
        page.setMenu(this);
        page.setHolder(this.holder);
        page.build();

        this.pages.put("landing", page);
    }

    /**
     * Checks if the menu is marked dirty.
     * 
     * @return True if it is, false if not
     */
    public boolean isDirty() {
        return this.isDirty;
    }

    /**
     * Gets a page from the menu.
     * 
     * @param ID
     *            The Id of a page
     * @return The page
     */
    public Page getPage(String ID) {
        return this.pages.get(ID);
    }

    /**
     * Gets the page currently opened on the page.
     * 
     * @return The page
     */
    public Page getCurrentPage() {
        return this.currentPage;
    }

    /**
     * Gets a collection of pages from the menu.
     * 
     * @return The collection of pages.
     */
    public Collection<Page> getPages() {
        return this.pages.values();
    }
}