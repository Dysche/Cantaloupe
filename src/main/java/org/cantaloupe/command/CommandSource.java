package org.cantaloupe.command;

import org.bukkit.command.CommandSender;
import org.cantaloupe.permission.IPermissionHolder;
import org.cantaloupe.text.Text;

public class CommandSource implements IPermissionHolder {
    private CommandSender handle = null;

    protected CommandSource(CommandSender handle) {
        this.handle = handle;
    }

    /**
     * Sends a message to the command source.
     * 
     * @param message
     *            The message
     */
    public void sendMessage(Text message) {
        this.handle.sendMessage(message.toLegacy());
    }

    /**
     * Sends a message to the command source.
     * 
     * @param message
     *            The message
     */
    public void sendMessage(String message) {
        this.sendMessage(Text.of(message));
    }

    /**
     * Sends a formatted message to the command source.
     * 
     * @param message
     *            The message
     */
    public void sendLegacyMessage(String message) {
        this.sendMessage(Text.fromLegacy(message));
    }

    /**
     * Checks if the command source has a permission node.
     * 
     * @return True if it does, false if not
     */
    @Override
    public boolean hasPermission(String node) {
        return this.handle.hasPermission(node);
    }

    /**
     * Checks if the command source is a player.
     * 
     * @return True if it is, false if not
     */
    public boolean isPlayer() {
        return this.handle instanceof org.bukkit.entity.Player;
    }

    /**
     * Gets the name of the command source.
     * 
     * @return The name
     */
    public String getName() {
        return this.handle.getName();
    }

    /**
     * Returns the handle of the command source.
     * 
     * @return The handle
     */
    public CommandSender toHandle() {
        return this.handle;
    }
}