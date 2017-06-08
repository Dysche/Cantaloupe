package org.cantaloupe.inventory.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.cantaloupe.player.Player;

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

    public static Page of() {
        return new Page();
    }

    public Page ID(String ID) {
        this.ID = ID;

        return this;
    }

    public Page name(String name) {
        this.name = name;

        return this;
    }

    public Page size(int size) {
        this.size = size;

        return this;
    }

    public Page button(Button button) {
        button.setPage(this);
        this.buttons.add(button);

        return this;
    }

    public void moveButton(Button button, int newSlot) {
        button.setSlot(newSlot);
    }

    public void refreshButton(Button button) {
        this.inventory.setItem(button.getSlot(), button.getIcon().toHandle());
    }

    public void removeButton(int slot) {
        this.buttons.remove(slot);

        if (this.inventory != null) {
            this.inventory.setItem(slot, null);
        }
    }

    public void removeButton(Button button) {
        this.buttons.remove(button.getSlot());

        if (this.inventory != null) {
            this.inventory.setItem(button.getSlot(), null);
        }
    }

    public void refresh() {
        this.clear();
        this.build();
    }

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

    public Menu getMenu() {
        return this.menu;
    }

    public Player getHolder() {
        return this.holder;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public Button getButton(int slot) {
        for (Button button : this.buttons) {
            if (button.getSlot() == slot) {
                return button;
            }
        }

        return null;
    }

    public List<Button> getButtons() {
        return this.buttons;
    }

    protected Inventory getInventory() {
        return this.inventory;
    }
}