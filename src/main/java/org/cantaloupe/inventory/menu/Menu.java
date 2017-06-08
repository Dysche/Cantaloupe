package org.cantaloupe.inventory.menu;

import java.util.Collection;
import java.util.HashMap;

import org.cantaloupe.player.Player;

public class Menu {
    private HashMap<String, Page> pages  = null;
    private Player                holder = null;

    private Menu(Player holder) {
        this.pages = new HashMap<String, Page>();
        this.holder = holder;
    }

    public static Menu of(Player holder) {
        return new Menu(holder);
    }

    public void landingPage(Page page) {
        page.setMenu(this);
        page.setHolder(this.holder);
        page.build();

        this.pages.put("landing", page);
    }

    public void page(Page page) {
        page.setMenu(this);
        page.setHolder(this.holder);
        page.build();

        this.pages.put(page.getID(), page);
    }

    public void open() {
        if (this.getPage("landing") != null) {
            this.showLandingPage();
        }
    }
    
    public void close() {
        this.holder.closeMenu();
    }

    public void showLandingPage() {
        this.holder.toHandle().openInventory(this.pages.get("landing").getInventory());
    }

    public void showPage(String ID) {
        if (this.pages.get(ID) != null) {
            this.holder.toHandle().openInventory(this.pages.get(ID).getInventory());
        }
    }

    public void removePage(String ID) {
        if (this.pages.containsKey(ID)) {
            this.pages.get(ID).clear();
            this.pages.remove(ID);
        }
    }

    public Page getPage(String ID) {
        return this.pages.get(ID);
    }

    public Collection<Page> getPages() {
        return this.pages.values();
    }
}