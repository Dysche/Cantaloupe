package org.cantaloupe.inventory.menu;

import java.util.function.BiConsumer;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.inventory.ItemStack;
import org.cantaloupe.service.services.ScheduleService;

/**
 * A class used to create a button.
 * 
 * @author Dylan Scheltens
 *
 */
public class Button {
    private int                       slot          = -1;
    private ItemStack                 icon          = null;
    private boolean                   canMove       = false;
    private BiConsumer<Button, Page>  clickConsumer = null;
    private BiConsumer<Integer, Page> moveConsumer  = null;

    private Page                      page          = null;

    private Button() {

    }

    /**
     * Creates and returns a new button.
     * 
     * @return The button
     */
    public static Button of() {
        return new Button();
    }

    /**
     * Called when the button has been clicked.
     */
    public void onClick() {
        if (this.clickConsumer != null) {
            this.clickConsumer.accept(this, this.page);
        }
    }

    /**
     * Called when the button has moved.
     */
    public void onMove() {
        if (this.moveConsumer != null) {
            this.moveConsumer.accept(this.slot, this.page);
        }
    }

    /**
     * Changes the icon of the button.
     * 
     * @param icon
     *            The icon
     */
    public void changeIcon(ItemStack icon) {
        this.icon = icon;

        /** TODO: Schedule Service */
        final Button button = this;
        Cantaloupe.getServiceManager().provide(ScheduleService.class).delay(this + ":changeIcon", new Runnable() {
            @Override
            public void run() {
                page.refreshButton(button);
            }
        });
    }

    /**
     * Sets the slot of the button.
     * 
     * @param slot
     *            The slot
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }

    /**
     * Sets the icon of the button.
     * 
     * @param icon
     *            The icon
     */
    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    /**
     * Sets the click consumer of the button.
     * 
     * @param consumer
     *            The consumer
     */
    public void setClickConsumer(BiConsumer<Button, Page> consumer) {
        this.clickConsumer = consumer;
    }

    /**
     * Sets the move consumer of the button.
     * 
     * @param consumer
     *            The consumer
     */
    public void setMoveConsumer(BiConsumer<Integer, Page> consumer) {
        this.moveConsumer = consumer;
    }

    /**
     * Sets whether or not the button can be moved.
     * 
     * @param canMove
     *            Whether or not the button cam be moved.
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    protected void setPage(Page page) {
        this.page = page;
    }

    /**
     * Checks if the button can move.
     * 
     * @return True if it can, false if not.
     */
    public boolean canMove() {
        return this.canMove;
    }

    /**
     * Gets the slot of the button.
     * 
     * @return The slot
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * Gets the icon of the button.
     * 
     * @return The icon
     */
    public ItemStack getIcon() {
        return this.icon;
    }
}