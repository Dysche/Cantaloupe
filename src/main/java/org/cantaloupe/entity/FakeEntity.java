package org.cantaloupe.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Entity;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.protocol.datawatcher.DataWatcher;
import org.cantaloupe.protocol.datawatcher.DataWatcherObject;
import org.cantaloupe.protocol.datawatcher.DataWatcherRegistry;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.BitFlags;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

public class FakeEntity {
    protected Object            handle   = null;
    protected final EntityType  type;
    protected ImmutableLocation location = null;
    protected byte              flags    = 0;

    protected FakeEntity(EntityType type, ImmutableLocation location, float headRotation, String customName, boolean customNameVisible, boolean invisible, boolean create) {
        this.type = type;
        this.location = location;

        if (create) {
            this.create(headRotation, customName, customNameVisible, invisible);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private void create(float headRotation, String customName, boolean customNameVisible, boolean invisible) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object entity = service.getNMSClass(this.type.getClassName()).getConstructor(service.NMS_WORLD_CLASS).newInstance(service.getWorldHandle(this.location.getWorld()));

            ReflectionHelper.invokeMethod("setPositionRotation", entity, new Class<?>[] {
                    double.class, double.class, double.class, float.class, float.class
            }, this.location.getPosition().x, this.location.getPosition().y, this.location.getPosition().z, this.location.getYaw(), this.location.getPitch());

            if (customName != null) {
                ReflectionHelper.invokeMethod("setCustomName", entity, customName);

                ReflectionHelper.invokeMethod("setCustomNameVisible", entity, new Class<?>[] {
                        boolean.class
                }, customNameVisible);
            }

            ReflectionHelper.invokeMethod("setInvisible", entity, new Class<?>[] {
                    boolean.class
            }, invisible);

            if (headRotation != -1f) {
                ReflectionHelper.invokeMethod("h", entity, new Class<?>[] {
                        float.class
                }, headRotation);
            }

            this.handle = entity;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        FakeEntityContainer.addEntity(this);
    }

    public void remove() {
        FakeEntityContainer.removeEntity(this);
    }

    public void spawn(Player player) {
        this.spawn(new Player[] {
                player
        });
    }

    public void spawn(Player... players) {
        this.spawn(Arrays.asList(players));
    }

    public void spawn(List<Player> players) {
        this.spawn((Collection<Player>) players);
    }

    public void spawn(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = nmsService.NMS_PACKET_OUT_SPAWNENTITYLIVING_CLASS.getConstructor(nmsService.NMS_ENTITY_LIVING_CLASS).newInstance(this.handle);

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.updatePassenger(players, this.getPassenger());
    }

    public void despawn(Player player) {
        this.despawn(new Player[] {
                player
        });
    }

    public void despawn(Player... players) {
        this.despawn(Arrays.asList(players));
    }

    public void despawn(List<Player> players) {
        this.despawn((Collection<Player>) players);
    }

    public void despawn(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = nmsService.NMS_PACKET_OUT_DESTROYENTITY_CLASS.getConstructor(int[].class).newInstance(new int[] {
                    this.getEntityID()
            });

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setLocation(Collection<Player> players, ImmutableLocation location) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setPositionRotation", this.handle, new Class<?>[] {
                    double.class, double.class, double.class, float.class, float.class
            }, location.getPosition().x, location.getPosition().y, location.getPosition().z, location.getYaw(), location.getPitch());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        try {
            Object teleportPacket = null;
            Object lookPacket = null;

            if (nmsService.getIntVersion() < 7) {
                teleportPacket = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT.newInstance();
                lookPacket = nmsService.NMS_PACKET_OUT_ENTITYLOOK.getConstructor(int.class, byte.class, byte.class, boolean.class).newInstance(this.getEntityID(), this.getFixRotation(location.getYaw()), this.getFixRotation(location.getPitch()), false);

                ReflectionHelper.setDeclaredField("a", teleportPacket, this.getEntityID());
                ReflectionHelper.setDeclaredField("b", teleportPacket, this.getFixPosition(location.getPosition().x));
                ReflectionHelper.setDeclaredField("c", teleportPacket, this.getFixPosition(location.getPosition().y));
                ReflectionHelper.setDeclaredField("d", teleportPacket, this.getFixPosition(location.getPosition().z));
                ReflectionHelper.setDeclaredField("e", teleportPacket, this.getFixRotation(location.getYaw()));
                ReflectionHelper.setDeclaredField("f", teleportPacket, this.getFixRotation(location.getPitch()));
            } else {
                teleportPacket = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(this.handle);
                lookPacket = nmsService.NMS_PACKET_OUT_ENTITYLOOK.getConstructor(int.class, byte.class, byte.class, boolean.class).newInstance(this.getEntityID(), this.getFixRotation(location.getYaw()), this.getFixRotation(location.getPitch()), false);
            }

            for (Player player : players) {
                packetService.sendPacket(player, teleportPacket);
                packetService.sendPacket(player, lookPacket);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.location = location;
    }

    public void setPosition(Collection<Player> players, Vector3d position) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setPosition", this.handle, new Class<?>[] {
                    double.class, double.class, double.class
            }, position.x, position.y, position.z);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        try {
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT.newInstance();

                ReflectionHelper.setDeclaredField("a", packet, this.getEntityID());
                ReflectionHelper.setDeclaredField("b", packet, this.getFixPosition(position.x));
                ReflectionHelper.setDeclaredField("c", packet, this.getFixPosition(position.y));
                ReflectionHelper.setDeclaredField("d", packet, this.getFixPosition(position.z));
                ReflectionHelper.setDeclaredField("e", packet, this.getFixRotation(this.location.getYaw()));
                ReflectionHelper.setDeclaredField("f", packet, this.getFixRotation(this.location.getPitch()));
            } else {
                packet = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(this.handle);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.location = ImmutableLocation.of(this.location.getWorld(), position);
    }

    public void setRotation(Collection<Player> players, Vector2f rotation) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setPositionRotation", this.handle, new Class<?>[] {
                    double.class, double.class, double.class, float.class, float.class
            }, this.location.getPosition().x, this.location.getPosition().y, this.location.getPosition().z, rotation.x, rotation.y);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        try {
            Object lookPacket = nmsService.NMS_PACKET_OUT_ENTITYLOOK.getConstructor(int.class, byte.class, byte.class, boolean.class).newInstance(this.getEntityID(), this.getFixRotation(location.getYaw()), this.getFixRotation(location.getPitch()), false);

            for (Player player : players) {
                packetService.sendPacket(player, lookPacket);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        this.location = ImmutableLocation.of(this.location.getWorld(), this.location.getPosition(), rotation);
    }

    public void setHeadRotation(Collection<Player> players, float headRotation) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("h", this.handle, new Class<?>[] {
                    float.class
            }, headRotation);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        try {
            Object lookPacket = null;

            if (nmsService.getIntVersion() < 7) {
                nmsService.NMS_PACKET_OUT_ENTITYHEADROTATION.getConstructor(int.class, byte.class).newInstance(this.getEntityID(), this.getFixRotation(headRotation));
            } else {
                nmsService.NMS_PACKET_OUT_ENTITYHEADROTATION.getConstructor(nmsService.NMS_ENTITY_CLASS, byte.class).newInstance(this.handle, this.getFixRotation(headRotation));
            }

            for (Player player : players) {
                packetService.sendPacket(player, lookPacket);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setCustomName(Collection<Player> players, String customName) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setCustomName", this.handle, customName);

            DataWatcher dataWatcher = DataWatcher.of(null);
            dataWatcher.register(DataWatcherObject.<String>of(2, DataWatcherRegistry.STRING), customName);

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), dataWatcher.toNMS(), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setCustomNameVisible(Collection<Player> players, boolean visible) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setCustomNameVisible", this.handle, visible);

            DataWatcher dataWatcher = DataWatcher.of(null);
            dataWatcher.register(DataWatcherObject.<Boolean>of(3, DataWatcherRegistry.BOOLEAN), visible);

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), dataWatcher.toNMS(), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setSilent(Collection<Player> players, boolean silent) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setSilent", this.handle, silent);

            DataWatcher dataWatcher = DataWatcher.of(null);
            dataWatcher.register(DataWatcherObject.<Boolean>of(4, DataWatcherRegistry.BOOLEAN), silent);

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), dataWatcher.toNMS(), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setNoGravity(Collection<Player> players, boolean noGravity) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod(nmsService.getIntVersion() < 11 ? "setGravity" : "setNoGravity", this.handle, new Class<?>[] {
                    boolean.class
            }, nmsService.getIntVersion() < 11 ? !noGravity : noGravity);

            DataWatcher dataWatcher = DataWatcher.of(null);
            dataWatcher.register(DataWatcherObject.<Boolean>of(5, DataWatcherRegistry.BOOLEAN), noGravity);

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), dataWatcher.toNMS(), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setOnFire(Collection<Player> players, boolean onFire) {
        try {
            ReflectionHelper.invokeMethod("setOnFire", this.handle, new Class<?>[] {
                    boolean.class
            }, onFire);

            if (onFire) {
                this.flags = BitFlags.setFlag(this.flags, (byte) 0x01);
            } else {
                this.flags = BitFlags.unsetFlag(this.flags, (byte) 0x01);
            }

            this.updateFlags(players);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setInvisible(Collection<Player> players, boolean invisible) {
        try {
            ReflectionHelper.invokeMethod("setInvisible", this.handle, new Class<?>[] {
                    boolean.class
            }, invisible);

            if (invisible) {
                this.flags = BitFlags.setFlag(this.flags, (byte) 0x20);
            } else {
                this.flags = BitFlags.unsetFlag(this.flags, (byte) 0x20);
            }

            this.updateFlags(players);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setGlowing(Collection<Player> players, boolean glowing) {
        try {
            ReflectionHelper.setField("glowing", this.handle, glowing);

            if (glowing) {
                this.flags = BitFlags.setFlag(this.flags, (byte) 0x40);
            } else {
                this.flags = BitFlags.unsetFlag(this.flags, (byte) 0x40);
            }

            this.updateFlags(players);
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void setPassenger(Collection<Player> players, FakeEntity other) {
        this.setPassenger(players, other.handle);
    }

    public void setPassenger(Collection<Player> players, Player player) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        this.setPassenger(players, nmsService.getEntityHandle(player.toHandle()));
    }

    public void setPassenger(Collection<Player> players, Entity entity) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        this.setPassenger(players, nmsService.getEntityHandle(entity));
    }

    public void setPassenger(Collection<Player> players, Object entity) {
        if (entity == null || this.hasPassenger()) {
            this.removePassenger(players);
        }

        try {
            ReflectionHelper.invokeMethod("add", ReflectionHelper.getField("passengers", this.handle), new Class<?>[] {
                    Object.class
            }, entity);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.updatePassenger(players, entity);
    }

    public void removePassenger(Collection<Player> players) {
        try {
            ReflectionHelper.invokeMethod("clear", ReflectionHelper.getField("passengers", this.handle));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.updatePassenger(players, null);
    }

    public boolean hasPassenger() {
        try {
            return (int) ReflectionHelper.invokeMethod("size", ReflectionHelper.getField("passengers", this.handle)) > 0;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return false;
    }

    protected void updatePassenger(Collection<Player> players, Object entity) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_ATTACHENTITY_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS, nmsService.NMS_ENTITY_CLASS).newInstance(this.handle, entity);
            } else {
                packet = nmsService.NMS_PACKET_OUT_MOUNT_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(this.handle);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateFlags(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            DataWatcher dataWatcher = DataWatcher.of(null);
            dataWatcher.register(DataWatcherObject.<Byte>of(0, DataWatcherRegistry.BYTE), this.flags);

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), dataWatcher.toNMS(), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public int getEntityID() {
        try {
            return (int) ReflectionHelper.invokeMethod("getId", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public ImmutableLocation getLocation() {
        return this.location;
    }

    public Vector3d getPosition() {
        return this.location.getPosition();
    }

    public Vector2f getRotation() {
        return this.location.getRotation();
    }

    public float getHeadRotation() {
        try {
            return (float) ReflectionHelper.invokeMethod("getHeadRotation", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0f;
    }

    public String getName() {
        try {
            return (String) ReflectionHelper.invokeMethod("getName", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Text getCustomName() {
        try {
            return Text.fromLegacy((String) ReflectionHelper.invokeMethod("getCustomName", this.handle));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean getCustomNameVisible() {
        try {
            return (boolean) ReflectionHelper.invokeMethod("getCustomNameVisible", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hasCustomName() {
        try {
            return (boolean) ReflectionHelper.invokeMethod("hasCustomName", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Object getPassenger() {
        try {
            List<?> passengers = (List<?>) ReflectionHelper.getField("passengers", this.handle);

            if (passengers.size() > 0) {
                return passengers.get(0);
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return false;
    }

    protected int getFixPosition(double d) {
        return (int) Math.floor(d * 32.0D);
    }

    protected byte getFixRotation(float f) {
        return (byte) ((int) (f * 256.0F / 360.0F));
    }

    public static class Builder {
        protected ImmutableLocation location          = null;
        protected World             world             = null;
        protected Vector3d          position          = null;
        protected Vector2f          rotation          = null;
        protected float             headRotation      = -1f;
        protected EntityType        type              = null;
        protected Text              customName        = null;
        protected boolean           customNameVisible = false, invisible = false;

        protected Builder() {

        }

        public Builder location(ImmutableLocation location) {
            this.location = location;

            return this;
        }

        public Builder world(World world) {
            this.world = world;

            return this;
        }

        public Builder position(Vector3d position) {
            this.position = position;

            return this;
        }

        public Builder rotation(Vector2f rotation) {
            this.rotation = rotation;

            return this;
        }

        public Builder headRotation(float headRotation) {
            this.headRotation = headRotation;

            return this;
        }

        public Builder type(EntityType type) {
            this.type = type;

            return this;
        }

        public Builder customName(Text customName) {
            this.customName = customName;

            return this;
        }

        public Builder customNameVisible(boolean customNameVisible) {
            this.customNameVisible = customNameVisible;

            return this;
        }

        public Builder invisible(boolean invisible) {
            this.invisible = invisible;

            return this;
        }

        public FakeEntity build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            return new FakeEntity(this.type, this.location, this.headRotation, this.customName != null ? this.customName.toLegacy() : null, this.customNameVisible, this.invisible, true);
        }
    }
}