package org.cantaloupe.command;

import org.bukkit.command.CommandSender;
import org.cantaloupe.permission.IPermissionHolder;
import org.cantaloupe.text.Text;

public class CommandSource implements IPermissionHolder {
    private CommandSender handle = null;

    protected CommandSource(CommandSender handle) {
        this.handle = handle;
    }

    public void sendMessage(Text text) {
        this.handle.sendMessage(text.toLegacy());
    }

    public void sendMessage(String message) {
        this.sendMessage(Text.of(message));
    }

    public void sendLegacyMessage(String message) {
        this.sendMessage(Text.fromLegacy(message));
    }

    @Override
    public boolean hasPermission(String node) {
        return this.handle.hasPermission(node);
    }

    public String getName() {
        return this.handle.getName();
    }

    public CommandSender getHandle() {
        return this.handle;
    }
}
