package org.cantaloupe.inventory.menu;

import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.inventory.ItemStack;

public class Button {
    private int                       slot          = -1;
    private ItemStack                 icon          = null;
    private boolean                   canMove       = false;
    private BiConsumer<Button, Page>  clickConsumer = null;
    private BiConsumer<Integer, Page> moveConsumer  = null;

    private Page                      page          = null;

    private Button() {

    }

    public static Button of() {
        return new Button();
    }

    public Button slot(int slot) {
        this.slot = slot;

        return this;
    }

    public Button icon(ItemStack icon) {
        this.icon = icon;

        return this;
    }

    public Button click(BiConsumer<Button, Page> consumer) {
        this.clickConsumer = consumer;

        return this;
    }

    public Button move(BiConsumer<Integer, Page> consumer) {
        this.moveConsumer = consumer;

        return this;
    }

    public Button canMove(boolean canMove) {
        this.canMove = canMove;

        return this;
    }

    public void onClick() {
        if(this.clickConsumer != null) {
            this.clickConsumer.accept(this, this.page);
        }
    }

    public void onMove() {
        if(this.moveConsumer != null) {
            this.moveConsumer.accept(this.slot, this.page);
        }
    }

    public void changeIcon(ItemStack icon) {
        this.icon = icon;
        
        /** TODO: Schedule Service */
        final Button button = this;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cantaloupe.getInstance(), new Runnable() {
            @Override
            public void run() {
                page.refreshButton(button);
            }
        });
    }

    protected void setPage(Page page) {
        this.page = page;
    }

    protected void setSlot(int slot) {
        this.slot = slot;
    }

    public boolean canMove() {
        return this.canMove;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getIcon() {
        return this.icon;
    }
}