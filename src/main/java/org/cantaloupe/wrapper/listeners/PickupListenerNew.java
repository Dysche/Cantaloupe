package org.cantaloupe.wrapper.listeners;

import java.util.Optional;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.permission.Allowable;
import org.cantaloupe.player.Player;

public class PickupListenerNew implements Listener {
    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle((org.bukkit.entity.Player) event.getEntity());
            if (playerOpt.isPresent()) {
                Player player = playerOpt.get();

                if (!player.isAllowed(Allowable.ITEM_PICKUP)) {
                    event.setCancelled(true);

                    return;
                }
            }
        }
    }
}