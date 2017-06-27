package org.cantaloupe.service.services;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.Service;

public class PacketService implements Service {
    private NMSService nmsService  = null;

    @Override
    public void load() {
        this.nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
    }

    @Override
    public void unload() {
        this.nmsService = null;
    }

    public void sendPacket(Player player, Object packet) {
        Object craftPlayer = this.nmsService.BUKKIT_CRAFTPLAYER_CLASS.cast(player.toHandle());

        try {
            Object handle = craftPlayer.getClass().getDeclaredMethod("getHandle").invoke(craftPlayer);
            Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);

            playerConnection.getClass().getDeclaredMethod("sendPacket", this.nmsService.NMS_PACKET_CLASS).invoke(playerConnection, packet);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "packet";
    }
}