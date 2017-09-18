package org.cantaloupe.service.services;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.IService;

/**
 * A service used to manage packets.
 * 
 * @author Dylan Scheltens
 *
 */
public class PacketService implements IService {
    private NMSService nmsService = null;

    @Override
    public void load() {
        this.nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
    }

    @Override
    public void unload() {
        this.nmsService = null;
    }

    /**
     * Sends a packet to the player.
     * 
     * @param player
     *            The player
     * @param packet
     *            The packet
     */
    public void sendPacket(Player player, Object packet) {
        Object playerConnection = this.nmsService.getPlayerConnection(player);

        try {
            playerConnection.getClass().getDeclaredMethod("sendPacket", this.nmsService.NMS_PACKET_CLASS).invoke(playerConnection, packet);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "packet";
    }
}