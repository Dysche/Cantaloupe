package org.cantaloupe.wrapper.listeners;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.inventory.menu.Button;
import org.cantaloupe.inventory.menu.Page;
import org.cantaloupe.permission.group.GroupManager;
import org.cantaloupe.player.Player;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = Player.of(event.getPlayer());
        Cantaloupe.getPlayerManager().addPlayer(player);
        player.onJoin();
        player.onLoad();

        event.setJoinMessage(null);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());

        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            player.onUnload();
            player.onLeave();

            Cantaloupe.getPlayerManager().removePlayer(player);
        }

        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            // Format
            event.setFormat("<" + GroupManager.getPrefixFor(player).toLegacy() + player.getName() + "> " + event.getMessage());
        }
    }

    @EventHandler
    public void onPlayerSwitchWorld(PlayerChangedWorldEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            player.onWorldSwitch(Cantaloupe.getWorldManager().getWorldFromHandle(event.getFrom()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle((org.bukkit.entity.Player) event.getWhoClicked());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            if (player.getCurrentMenu() != null) {
                for (Page page : player.getCurrentMenu().getPages()) {
                    for (Button button : page.getButtons()) {
                        if (button.canMove()) {
                            if (button.getIcon().equals(event.getCursor())) {
                                if (!page.isButton(event.getSlot())) {
                                    if (button.getSlot() != event.getSlot() && event.getSlot() != -999) {
                                        page.moveButton(button, event.getSlot());
                                        button.onMove();
                                    }
                                } else {
                                    if (button != page.getButton(event.getSlot())) {
                                        event.setCancelled(true);
                                    }
                                }
                            }
                        } else {
                            if (button.getSlot() == event.getSlot()) {
                                event.setCurrentItem(null);
                                event.setCancelled(true);

                                button.onClick();
                                page.refreshButton(button);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            if (player.getCurrentMenu() != null) {
                for (Page page : player.getCurrentMenu().getPages()) {
                    for (Button button : page.getButtons()) {
                        if (button.getIcon().equals(event.getItemDrop().getItemStack())) {
                            event.getItemDrop().remove();

                            /** TODO: Schedule Service */
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Cantaloupe.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    page.refreshButton(button);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            player.getWorld().tickPlayer(player);
        }
    }
}