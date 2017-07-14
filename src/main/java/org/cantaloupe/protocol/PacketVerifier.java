package org.cantaloupe.protocol;

import org.cantaloupe.entity.FakeEntity;
import org.cantaloupe.player.Player;

public class PacketVerifier {
    public static boolean verifyEntityUsePacket(Player player, FakeEntity entity) {
        if(player.getLocation().getPosition().distance(entity.getLocation().getPosition()) < 4.5) {
            return true;
        }
        
        return false;
    }
}