package org.cantaloupe.service.services;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.Service;
import org.cantaloupe.service.services.ParticleService.ParticleData;
import org.cantaloupe.service.services.ParticleService.ParticleProperty;
import org.cantaloupe.service.services.ParticleService.ParticleType;
import org.cantaloupe.user.User;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.location.Location;
import org.joml.Vector3f;

public class PacketService implements Service {
    private NMSService nmsService          = null;
    private Class<?>   packetClass         = null;

    // Particle Classes
    private Class<?>   particlePacketClass = null;
    private Class<?>   enumParticleClass   = null;
    private Enum<?>[]  enumParticleValues  = null;

    @Override
    public void load() {
        this.nmsService = Cantaloupe.getServiceManager().getService(NMSService.class);
        this.packetClass = this.nmsService.getNMSClass("Packet");

        // Particle Classes
        this.particlePacketClass = this.nmsService.getNMSClass(this.nmsService.getIntVersion() < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");

        if (this.nmsService.getIntVersion() > 7) {
            this.enumParticleClass = this.nmsService.getNMSClass("EnumParticle");
        }

        try {
            this.enumParticleValues = (Enum<?>[]) this.enumParticleClass.getDeclaredMethod("values").invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unload() {
        this.nmsService = null;
        this.packetClass = null;

        this.enumParticleClass = null;
        this.enumParticleValues = null;
    }

    protected void sendParticlePacket(User[] users, ParticleType type, ParticleData data, Location location, Vector3f offset, boolean longDistance, float speed, int amount) {
        float offsetX = offset.x;
        float offsetY = offset.y;
        float offsetZ = offset.z;

        if (type.hasProperty(ParticleProperty.COLORABLE)) {
            if (offsetX > 0f) {
                offsetX /= 255f;
            } else {
                offsetX = Float.MIN_NORMAL;
            }

            offsetY /= 255f;
            offsetZ /= 255f;
        }

        try {
            Object packet = this.particlePacketClass.newInstance();

            if (this.nmsService.getIntVersion() < 7) {
                String name = type.getName();

                if (data != null) {
                    name += data.getPacketDataString();
                }

                ReflectionHelper.setPrivateField("a", packet, name);
            } else {
                ReflectionHelper.setPrivateField("a", packet, this.enumParticleValues[type.getID()]);
                ReflectionHelper.setPrivateField("j", packet, longDistance);

                if (data != null) {
                    int[] packetData = data.getPacketData();

                    ReflectionHelper.setPrivateField("k", packet, type == ParticleType.ITEM_CRACK ? packetData : new int[] {
                            packetData[0] | (packetData[1] << 12)
                    });
                }
            }

            ReflectionHelper.setPrivateField("b", packet, (float) location.getPosition().x);
            ReflectionHelper.setPrivateField("c", packet, (float) location.getPosition().y);
            ReflectionHelper.setPrivateField("d", packet, (float) location.getPosition().z);
            ReflectionHelper.setPrivateField("e", packet, offsetX);
            ReflectionHelper.setPrivateField("f", packet, offsetY);
            ReflectionHelper.setPrivateField("g", packet, offsetZ);
            ReflectionHelper.setPrivateField("h", packet, speed);
            ReflectionHelper.setPrivateField("i", packet, amount);

            for (User user : users) {
                this.sendPacket(user, packet);
            }
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void sendPacket(User user, Object packet) {
        Object craftPlayer = this.nmsService.getNMSBukkitClass("entity.CraftPlayer").cast(user.toHandle());

        try {
            Object handle = craftPlayer.getClass().getDeclaredMethod("getHandle").invoke(craftPlayer);
            Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);

            playerConnection.getClass().getDeclaredMethod("sendPacket", this.packetClass).invoke(playerConnection, packet);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "packet";
    }
}