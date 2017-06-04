package org.cantaloupe.services.particle;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.cantaloupe.user.User;
import org.cantaloupe.world.location.Location;
import org.joml.Vector3f;

import net.minecraft.server.v1_11_R1.EnumParticle;
import net.minecraft.server.v1_11_R1.Packet;
import net.minecraft.server.v1_11_R1.PacketPlayOutWorldParticles;

public class ParticleService {
    public static void display(User user, ParticleType type, Vector3f position, Vector3f offset, boolean longDistance, int speed, int amount) {
        CraftPlayer player = (CraftPlayer) user.toHandle();
        player.getHandle().playerConnection.sendPacket(createPacket(type, position, offset, longDistance, speed, amount));
    }
    
    public static void display(User user, ParticleType type, Location location, Vector3f offset, boolean longDistance, int speed, int amount) {
        CraftPlayer player = (CraftPlayer) user.toHandle();
        player.getHandle().playerConnection.sendPacket(createPacket(type, new Vector3f((float)location.getPosition().x, (float)location.getPosition().y, (float)location.getPosition().z), offset, longDistance, speed, amount));
    }
    
    private static Packet<?> createPacket(ParticleType type, Vector3f position, Vector3f offset, boolean longDistance, int speed, int amount) {
        float offsetX = offset.x;
        float offsetY = offset.y;
        float offsetZ = offset.z;
        
        if(type.isColorable()) {
            if(offsetX > 0) {
                offsetX /= 255f;
            } else {
                offsetX = Float.MIN_NORMAL;
            }
            
            offsetY /= 255f;
            offsetZ /= 255f;
        }

        return new PacketPlayOutWorldParticles(type.getType(), longDistance, position.x, position.y, position.z, offsetX, offsetY, offsetZ, speed, amount);
    }

    public enum ParticleType {
        // Values
        BARRIER(EnumParticle.BARRIER, false),
        REDSTONE(EnumParticle.REDSTONE, true);
        
        // Enum Body
        private final EnumParticle type;
        private final boolean      colorable;

        private ParticleType(EnumParticle type, boolean colorable) {
            this.type = type;
            this.colorable = colorable;
        }

        public boolean isColorable() {
            return this.colorable;
        }

        public EnumParticle getType() {
            return this.type;
        }
    }
}