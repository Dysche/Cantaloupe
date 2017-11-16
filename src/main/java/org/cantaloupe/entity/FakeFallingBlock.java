package org.cantaloupe.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.protocol.datawatcher.DataWatcher;
import org.cantaloupe.protocol.datawatcher.DataWatcherObject;
import org.cantaloupe.protocol.datawatcher.DataWatcherRegistry;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

/**
 * A class used to create a "fake" falling block entity.
 * 
 * @author Dylan Scheltens
 *
 */
public class FakeFallingBlock extends FakeEntity {
    private final Material material;
    private final byte     data;

    private FakeFallingBlock(ImmutableLocation location, BlockFace blockFace, Material material, byte data) {
        super(EntityType.FALLING_BLOCK, location, blockFace, 0f, null, false, false, false);

        this.material = material;
        this.data = data;

        this.create(0f, null, false, false);
    }

    @Override
    protected void create(float headRotation, String customName, boolean customNameVisible, boolean invisible) {
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

            if (headRotation != Float.MIN_VALUE) {
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

    @Override
    public void spawn(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object spawnPacket = nmsService.NMS_PACKET_OUT_SPAWNENTITY_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS, int.class, int.class).newInstance(this.handle, 70, nmsService.getCombinedID(this.material, this.data));

            for (Player player : players) {
                packetService.sendPacket(player, spawnPacket);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.updatePassenger(players, this.getPassenger());
    }

    /**
     * Creates and returns a new builder.
     * 
     * @return The builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Sets the material of the entity for a collection of players.
     * 
     * @param players
     *            The collection of players
     * @param material
     *            The material
     */
    public void setMaterial(Collection<Player> players, Material material) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            DataWatcher watcher = DataWatcher.of(this.handle);
            watcher.register(DataWatcherObject.of(12, DataWatcherRegistry.INTEGER), nmsService.getBlockData(material, this.data));

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), ReflectionHelper.getDeclaredField("datawatcher", this.handle), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the data of the entity for a collection of players.
     * 
     * @param players
     *            The collection of players
     * @param data
     *            The data
     */
    public void setData(Collection<Player> players, byte data) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            DataWatcher watcher = DataWatcher.of(this.handle);
            watcher.register(DataWatcherObject.of(12, DataWatcherRegistry.INTEGER), nmsService.getBlockData(this.material, data));

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), ReflectionHelper.getDeclaredField("datawatcher", this.handle), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static final class Builder extends FakeEntity.Builder {
        private Material material = null;
        private byte     data     = 0;

        private Builder() {

        }

        /**
         * Sets the location of the builder.
         * 
         * @param location
         *            The location
         * @return The builder
         */
        @Override
        public Builder location(ImmutableLocation location) {
            this.location = location;

            return this;
        }

        /**
         * Sets the world of the builder.
         * 
         * @param world
         *            The world
         * @return The builder
         */
        @Override
        public Builder world(World world) {
            this.world = world;

            return this;
        }

        /**
         * Sets the position of the builder.
         * 
         * @param position
         *            The position
         * @return The builder
         */
        @Override
        public Builder position(Vector3d position) {
            this.position = position;

            return this;
        }

        /**
         * Sets the rotation of the builder.
         * 
         * @param rotation
         *            The rotation
         * @return The builder
         */
        @Override
        public Builder rotation(Vector2f rotation) {
            this.rotation = rotation;

            return this;
        }

        /**
         * Sets the block face of the builder.
         * 
         * @param blockFace
         *            The block face
         * @return The builder
         */
        @Override
        public Builder facing(BlockFace blockFace) {
            this.blockFace = blockFace;

            return this;
        }

        @Deprecated
        @Override
        public Builder headRotation(float headRotation) {
            this.headRotation = headRotation;

            return this;
        }

        public Builder material(Material material) {
            this.material = material;

            return this;
        }

        public Builder data(byte data) {
            this.data = data;

            return this;
        }

        @Deprecated
        public Builder type(EntityType type) {
            this.type = type;

            return this;
        }

        @Deprecated
        @Override
        public Builder customName(Text customName) {
            this.customName = customName;

            return this;
        }

        @Deprecated
        @Override
        public Builder customNameVisible(boolean customNameVisible) {
            this.customNameVisible = customNameVisible;

            return this;
        }

        @Deprecated
        @Override
        public Builder invisible(boolean invisible) {
            this.invisible = invisible;

            return this;
        }

        /**
         * Creates and returns a new entity from the builder.
         * 
         * @return The entity
         */
        public FakeFallingBlock build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            return new FakeFallingBlock(this.location, this.blockFace, this.material, this.data);
        }
    }
}