package org.cantaloupe.wrapper.listeners;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.permission.Allowable;
import org.cantaloupe.player.Player;

@SuppressWarnings("deprecation")
public class PickupListenerOld implements Listener {
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            if (!player.isAllowed(Allowable.ITEM_PICKUP)) {
                event.setCancelled(true);

                return;
            }
        }
    }
}