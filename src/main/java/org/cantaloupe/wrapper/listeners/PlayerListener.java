package org.cantaloupe.wrapper.listeners;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.inventory.menu.Button;
import org.cantaloupe.inventory.menu.Page;
import org.cantaloupe.permission.Allowable;
import org.cantaloupe.permission.group.GroupManager;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.ScheduleService;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = Player.of(event.getPlayer());
        Cantaloupe.getPlayerManager().addPlayer(player);
        player.onLoad();
        player.onJoin();

        if (!event.getPlayer().hasPlayedBefore()) {
            player.onFirstJoin();
        }

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());

        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            player.onLeave();
            player.onUnload();

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
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
            event.setFormat("<" + GroupManager.getPrefixFor(player).toLegacy() + player.getName() + "> %2$s");
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

                            Cantaloupe.getServiceManager().provide(ScheduleService.class).delay(player.getUUID() + ":" + ":menuItemDrop", new Runnable() {
                                @Override
                                public void run() {
                                    page.refreshButton(button);
                                }
                            });

                            return;
                        }
                    }
                }
            }

            if (!player.isAllowed(Allowable.ITEM_DROP)) {
                event.setCancelled(true);

                return;
            }
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            if (!player.isAllowed(Allowable.BLOCK_PLACE)) {
                event.setCancelled(true);

                return;
            }
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            if (!player.isAllowed(Allowable.BLOCK_BREAK)) {
                event.setCancelled(true);

                return;
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            if (player.isAllowed(Allowable.INTERACT_ALL)) {
                return;
            }
            
            if (player.isAllowed(Allowable.INTERACT_BLOCK)) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    return;
                }
            }
            
            if (player.isAllowed(Allowable.INTERACT_AIR)) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    return;
                }
            }
            
            if (player.isAllowed(Allowable.INTERACT_PHYSICAL)) {
                if (event.getAction() == Action.PHYSICAL) {
                    return;
                }
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromHandle(event.getPlayer());
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();

            if (!player.isAllowed(Allowable.ITEM_CONSUME)) {
                event.setCancelled(true);

                return;
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