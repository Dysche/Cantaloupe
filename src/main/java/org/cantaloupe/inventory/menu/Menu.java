package org.cantaloupe.inventory.menu;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.cantaloupe.player.Player;
import org.cantaloupe.screen.SignInput;
import org.cantaloupe.text.Text;

/**
 * A class used to create a menu.
 * 
 * @author Dylan Scheltens
 *
 */
public class Menu {
    private Player                      holder        = null;
    private final HashMap<String, Page> pages;

    private Page                        currentPage   = null;
    private boolean                     isDirty       = false;

    private Consumer<Menu>              closeConsumer = null;

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
        this.holder.openMenu(this);
    }

    /**
     * Closes the menu.
     */
    public void close() {
        this.holder.closeMenu();
    }

    public void onClose() {
        if (this.closeConsumer != null) {
            this.closeConsumer.accept(this);
        }
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

    public void showSignInput(BiConsumer<Player, List<String>> consumer) {
        this.isDirty = true;

        SignInput.of(this.holder).setInputConsumer((player, lines) -> {
            consumer.accept(player, lines);

            this.isDirty = false;
        }).show();
    }

    public void showSignInput(BiConsumer<Player, List<String>> consumer, Text... lines) {
        this.isDirty = true;

        SignInput.of(this.holder, Arrays.asList(lines)).setInputConsumer((player, l) -> {
            consumer.accept(player, l);

            this.isDirty = false;
        }).show();
    }

    public void showSignInput(BiConsumer<Player, List<String>> consumer, List<Text> lines) {
        this.isDirty = true;

        SignInput.of(this.holder, lines).setInputConsumer((player, l) -> {
            consumer.accept(player, l);

            this.isDirty = false;
        }).show();
    }

    /**
     * Adds a page to the menu.
     * 
     * @param page
     *            The page
     * 
     * @return The menu
     */
    public Menu addPage(Page page) {
        page.setMenu(this);
        page.setHolder(this.holder);
        page.build();

        this.pages.put(page.getID(), page);

        return this;
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
     * 
     * @return The menu
     */
    public Menu setLandingPage(Page page) {
        page.setMenu(this);
        page.setHolder(this.holder);
        page.build();

        this.pages.put("landing", page);

        return this;
    }

    /**
     * Sets the close consumer of the menu.
     * 
     * @param closeConsumer
     *            The consumer
     * 
     * @return The menu
     */
    public Menu setCloseConsumer(Consumer<Menu> closeConsumer) {
        this.closeConsumer = closeConsumer;

        return this;
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
     * Gets the landing page of the menu.
     * 
     * @return The page
     */
    public Page getLandingPage() {
        return this.pages.get("landing");
    }

    /**
     * Gets the page currently opened on the menu.
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