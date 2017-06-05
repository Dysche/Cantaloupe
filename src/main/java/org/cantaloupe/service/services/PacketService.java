package org.cantaloupe.service.services;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.Service;
import org.cantaloupe.service.services.ParticleService.ParticleType;
import org.cantaloupe.user.User;
import org.cantaloupe.util.ReflectionHelper;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class PacketService implements Service {
    private NMSService nmsService        = null;
    private Class<?>   packetClass       = null;
    private Class<?>   enumParticleClass = null;

    @Override
    public void load() {
        this.nmsService = Cantaloupe.getServiceManager().getService(NMSService.class);
        this.packetClass = this.nmsService.getNMSClass("Packet");
        this.enumParticleClass = this.nmsService.getNMSClass("EnumParticle");
    }

    @Override
    public void unload() {
        this.nmsService = null;
    }

    public void sendParticlePacket(User user, ParticleType type, Vector3d position, Vector3f offset, boolean longDistance, float speed, int amount, int... data) {
        float offsetX = offset.x;
        float offsetY = offset.y;
        float offsetZ = offset.z;

        if (type.getRequiredVersion() != -1) {
            if (type.getRequiredVersion() > this.nmsService.getVersion()) {
                return;
            }
        }

        if (type.isColorable()) {
            if (offsetX > 0) {
                offsetX /= 255f;
            } else {
                offsetX = Float.MIN_NORMAL;
            }

            offsetY /= 255f;
            offsetZ /= 255f;
        }

        try {
            Class<?> packetClass = this.nmsService.getNMSClass(this.nmsService.getVersion() < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
            Object packet = packetClass.newInstance();

            if (this.nmsService.getVersion() < 7) {
                String name = type.getName();

                if (data.length > 0) {
                    name += "_" + data[0] + "_" + data[1];
                }

                ReflectionHelper.setPrivateField("a", packet, name);
            } else {
                Enum<?>[] values = (Enum<?>[]) this.enumParticleClass.getDeclaredMethod("values").invoke(null);

                ReflectionHelper.setPrivateField("a", packet, values[type.getID()]);
                ReflectionHelper.setPrivateField("j", packet, longDistance);

                if (data.length > 0) {
                    ReflectionHelper.setPrivateField("k", packet, type == ParticleType.ITEM_CRACK ? data : new int[] {
                            data[0] | (data[1] << 12)
                    });
                }
            }

            ReflectionHelper.setPrivateField("b", packet, (float) position.x);
            ReflectionHelper.setPrivateField("c", packet, (float) position.y);
            ReflectionHelper.setPrivateField("d", packet, (float) position.z);
            ReflectionHelper.setPrivateField("e", packet, offsetX);
            ReflectionHelper.setPrivateField("f", packet, offsetY);
            ReflectionHelper.setPrivateField("g", packet, offsetZ);
            ReflectionHelper.setPrivateField("h", packet, speed);
            ReflectionHelper.setPrivateField("i", packet, amount);

            this.sendPacket(user, packet);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
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