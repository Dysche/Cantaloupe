package org.cantaloupe.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.block.BlockFace;
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
import org.cantaloupe.util.MathUtils;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

/**
 * A class used to create a "fake" entity.
 * 
 * @author Dylan Scheltens
 *
 */
public class FakeEntity {
    protected Object            handle    = null;
    protected final EntityType  type;
    protected ImmutableLocation location  = null;
    protected BlockFace         blockFace = null;
    protected byte              flags     = 0;

    protected FakeEntity(EntityType type, ImmutableLocation location, BlockFace blockFace, float headRotation, String customName, boolean customNameVisible, boolean invisible, boolean create) {
        this.type = type;
        this.location = location;
        this.blockFace = blockFace != null ? blockFace.getOppositeFace() : null;

        if (create) {
            this.create(headRotation, customName, customNameVisible, invisible);
        }
    }

    /**
     * Creates and returns a new builder.
     * 
     * @return The builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    protected void create(float headRotation, String customName, boolean customNameVisible, boolean invisible) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object entity = service.getNMSClass(this.type.getClassName()).getConstructor(service.NMS_WORLD_CLASS).newInstance(service.getWorldHandle(this.location.getWorld()));

            ReflectionHelper.invokeMethod("setPositionRotation", entity, new Class<?>[] {
                    double.class, double.class, double.class, float.class, float.class
            }, this.location.getPosition().x, this.location.getPosition().y, this.location.getPosition().z, this.blockFace != null ? (this.blockFace != BlockFace.UP && this.blockFace != BlockFace.DOWN ? MathUtils.faceToYaw(this.blockFace) : 0f) : 0f,
                    this.blockFace != null ? (this.blockFace == BlockFace.UP ? 90f : this.blockFace == BlockFace.DOWN ? -90f : 0f) : this.location.getPitch());

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

    /**
     * Removes the entity.
     */
    public void remove() {
        FakeEntityContainer.removeEntity(this);
    }

    /**
     * Spawns the entity for the player.
     * 
     * @param player
     *            The player
     */
    public void spawn(Player player) {
        this.spawn(new Player[] {
                player
        });
    }

    /**
     * Spawns the entity for an array of players.
     * 
     * @param players
     *            The array of players
     */
    public void spawn(Player... players) {
        this.spawn(Arrays.asList(players));
    }

    /**
     * Spawns the entity for a list of players.
     * 
     * @param players
     *            The list of players
     */
    public void spawn(List<Player> players) {
        this.spawn((Collection<Player>) players);
    }

    /**
     * Spawns the entity for a collection of players.
     * 
     * @param players
     *            The collection of players
     */
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

    /**
     * Despawns the entity for the player.
     * 
     * @param player
     *            The player
     */
    public void despawn(Player player) {
        this.despawn(new Player[] {
                player
        });
    }

    /**
     * Despawns the entity for an array of players.
     * 
     * @param players
     *            The array of players
     */
    public void despawn(Player... players) {
        this.despawn(Arrays.asList(players));
    }

    /**
     * Despawns the entity for a list of players.
     * 
     * @param players
     *            The list of players
     */
    public void despawn(List<Player> players) {
        this.despawn((Collection<Player>) players);
    }

    /**
     * Despawns the entity for a collection of players.
     * 
     * @param players
     *            The collection of players
     */
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

    public void animate(Collection<Player> players, int animation) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_ANIMATION_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS, byte.class).newInstance(this.handle, (byte) animation);
            } else {
                packet = nmsService.NMS_PACKET_OUT_ANIMATION_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS, int.class).newInstance(this.handle, animation);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the location of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param location
     *            The location
     */
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
                teleportPacket = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT_CLASS.newInstance();
                lookPacket = nmsService.NMS_PACKET_OUT_ENTITYLOOK_CLASS.getConstructor(int.class, byte.class, byte.class, boolean.class).newInstance(this.getEntityID(), this.getFixRotation(location.getYaw()), this.getFixRotation(location.getPitch()), false);

                ReflectionHelper.setDeclaredField("a", teleportPacket, this.getEntityID());
                ReflectionHelper.setDeclaredField("b", teleportPacket, this.getFixPosition(location.getPosition().x));
                ReflectionHelper.setDeclaredField("c", teleportPacket, this.getFixPosition(location.getPosition().y));
                ReflectionHelper.setDeclaredField("d", teleportPacket, this.getFixPosition(location.getPosition().z));
                ReflectionHelper.setDeclaredField("e", teleportPacket, this.getFixRotation(location.getYaw()));
                ReflectionHelper.setDeclaredField("f", teleportPacket, this.getFixRotation(location.getPitch()));
            } else {
                teleportPacket = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(this.handle);
                lookPacket = nmsService.NMS_PACKET_OUT_ENTITYLOOK_CLASS.getConstructor(int.class, byte.class, byte.class, boolean.class).newInstance(this.getEntityID(), this.getFixRotation(location.getYaw()), this.getFixRotation(location.getPitch()), false);
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

    /**
     * Sets the position of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param position
     *            The position
     */
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
                packet = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT_CLASS.newInstance();

                ReflectionHelper.setDeclaredField("a", packet, this.getEntityID());
                ReflectionHelper.setDeclaredField("b", packet, this.getFixPosition(position.x));
                ReflectionHelper.setDeclaredField("c", packet, this.getFixPosition(position.y));
                ReflectionHelper.setDeclaredField("d", packet, this.getFixPosition(position.z));
                ReflectionHelper.setDeclaredField("e", packet, this.getFixRotation(this.location.getYaw()));
                ReflectionHelper.setDeclaredField("f", packet, this.getFixRotation(this.location.getPitch()));
            } else {
                packet = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(this.handle);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.location = ImmutableLocation.of(this.location.getWorld(), position, this.location.getRotation());
    }

    /**
     * Sets the rotation of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param rotation
     *            The rotation
     */
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
            Object lookPacket = nmsService.NMS_PACKET_OUT_ENTITYLOOK_CLASS.getConstructor(int.class, byte.class, byte.class, boolean.class).newInstance(this.getEntityID(), this.getFixRotation(rotation.x), this.getFixRotation(rotation.y), false);

            for (Player player : players) {
                packetService.sendPacket(player, lookPacket);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        this.location = ImmutableLocation.of(this.location.getWorld(), this.location.getPosition(), rotation);
        this.blockFace = MathUtils.rotationToFace(rotation);
    }

    /**
     * Sets the rotation of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param rotation
     *            The rotation
     */
    public void setBlockFace(Collection<Player> players, BlockFace blockFace) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setPositionRotation", this.handle, new Class<?>[] {
                    double.class, double.class, double.class, float.class, float.class
            }, this.location.getPosition().x, this.location.getPosition().y, this.location.getPosition().z, blockFace != BlockFace.UP && blockFace != BlockFace.DOWN ? MathUtils.faceToYaw(blockFace) : 0, blockFace == BlockFace.UP ? 90 : blockFace == BlockFace.DOWN ? -90 : 0);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        try {
            Object lookPacket = nmsService.NMS_PACKET_OUT_ENTITYLOOK_CLASS.getConstructor(int.class, byte.class, byte.class, boolean.class).newInstance(this.getEntityID(), this.getFixRotation(blockFace != BlockFace.UP && blockFace != BlockFace.DOWN ? MathUtils.faceToYaw(blockFace) : 0),
                    this.getFixRotation(blockFace == BlockFace.UP ? 90 : blockFace == BlockFace.DOWN ? -90 : 0), false);

            for (Player player : players) {
                packetService.sendPacket(player, lookPacket);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        this.blockFace = blockFace;
        this.location = ImmutableLocation.of(this.location.getWorld(), this.location.getPosition(), new Vector2f(blockFace != BlockFace.UP && blockFace != BlockFace.DOWN ? MathUtils.faceToYaw(blockFace) : 0, blockFace == BlockFace.UP ? 90 : blockFace == BlockFace.DOWN ? -90 : 0));
    }

    /**
     * Sets the head rotation of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param headRotation
     *            The head rotation
     */
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
                nmsService.NMS_PACKET_OUT_ENTITYHEADROTATION_CLASS.getConstructor(int.class, byte.class).newInstance(this.getEntityID(), this.getFixRotation(headRotation));
            } else {
                nmsService.NMS_PACKET_OUT_ENTITYHEADROTATION_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS, byte.class).newInstance(this.handle, this.getFixRotation(headRotation));
            }

            for (Player player : players) {
                packetService.sendPacket(player, lookPacket);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the custom name of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param customName
     *            The custom name
     */
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

    /**
     * Sets whether or not the entity's custom name is visible for the
     * collection of players.
     * 
     * @param players
     *            The collection of players
     * @param visible
     *            Whether or not the custom name is visible
     */
    public void setCustomNameVisible(Collection<Player> players, boolean visible) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod("setCustomNameVisible", this.handle, new Class<?>[] {
                    boolean.class
            }, visible);

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

    /**
     * Sets whether or not the entity is silent for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param silent
     *            Whether or not the entity is silent
     */
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

    /**
     * Sets whether or not the entity has gravity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param gravity
     *            Whether or not the entity has gravity
     */
    public void setGravity(Collection<Player> players, boolean gravity) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            ReflectionHelper.invokeMethod(nmsService.getIntVersion() < 11 ? "setGravity" : "setNoGravity", this.handle, new Class<?>[] {
                    boolean.class
            }, nmsService.getIntVersion() < 11 ? gravity : !gravity);

            DataWatcher dataWatcher = DataWatcher.of(null);
            dataWatcher.register(DataWatcherObject.<Boolean>of(5, DataWatcherRegistry.BOOLEAN), !gravity);

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), dataWatcher.toNMS(), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets whether or not the entity is on fire for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param onFire
     *            Whether or not the entity is on fire
     */
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

    /**
     * Sets whether or not the entity is invisible for the collection of
     * players.
     * 
     * @param players
     *            The collection of players
     * @param invisible
     *            Whether or not the entity is invisible
     */
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

    /**
     * Sets whether or not the entity is glowing for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param glowing
     *            Whether or not the entity is glowing
     */
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

    /**
     * Sets the passenger of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param other
     *            The passenger
     */
    public void setPassenger(Collection<Player> players, FakeEntity other) {
        this.setPassenger(players, other.handle);
    }

    /**
     * Sets the passenger of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param player
     *            The passenger
     */
    public void setPassenger(Collection<Player> players, Player player) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        this.setPassenger(players, nmsService.getEntityHandle(player.toHandle()));
    }

    /**
     * Sets the passenger of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param entity
     *            The passenger
     */
    public void setPassenger(Collection<Player> players, Entity entity) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        this.setPassenger(players, nmsService.getEntityHandle(entity));
    }

    /**
     * Sets the passenger of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param entity
     *            The passenger
     */
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

    /**
     * Removes the passenger from the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     */
    public void removePassenger(Collection<Player> players) {
        try {
            ReflectionHelper.invokeMethod("clear", ReflectionHelper.getField("passengers", this.handle));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.updatePassenger(players, null);
    }

    /**
     * Checks if the entity has a passenger.
     * 
     * @return True if it does, false if not.
     */
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

    /**
     * Gets the handle of the entity.
     * 
     * @return The handle
     */
    public Object toHandle() {
        return this.handle;
    }

    /**
     * Gets the ID of the entity.
     * 
     * @return The ID
     */
    public int getEntityID() {
        try {
            return (int) ReflectionHelper.invokeMethod("getId", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Gets the location of the entity.
     * 
     * @return The location
     */
    public ImmutableLocation getLocation() {
        return this.location;
    }

    /**
     * Gets the object location of the entity.
     * 
     * @return The location
     */
    public ImmutableLocation getLocationForObject() {
        return this.location.subtract(0.5, 0, 0.5);
    }

    /**
     * Gets the position of the entity.
     * 
     * @return The position
     */
    public Vector3d getPosition() {
        return this.location.getPosition();
    }

    /**
     * Gets the object position of the entity.
     * 
     * @return The position
     */
    public Vector3d getPositionForObject() {
        return this.location.getPosition().sub(0.5, 0, 0.5);
    }

    /**
     * Gets the rotation of the entity.
     * 
     * @return The rotation
     */
    public Vector2f getRotation() {
        return this.location.getRotation();
    }

    /**
     * Gets the block face of the entity.
     * 
     * @return The block face
     */
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    /**
     * Gets the head rotation of the entity.
     * 
     * @return The head rotation
     */
    public float getHeadRotation() {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return (float) ReflectionHelper.invokeMethod("getHeadRotation", this.handle, nmsService.NMS_ENTITY_CLASS);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0f;
    }

    /**
     * Gets the name of the entity.
     * 
     * @return The name
     */
    public String getName() {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return (String) ReflectionHelper.invokeMethod("getName", this.handle, nmsService.NMS_ENTITY_CLASS);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the custom name of the entity.
     * 
     * @return The custom name
     */
    public String getCustomName() {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return (String) ReflectionHelper.invokeMethod("getCustomName", this.handle, nmsService.NMS_ENTITY_CLASS);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Checks if the custom name is visible.
     * 
     * @return True if it is, false if not
     */
    public boolean isCustomNameVisible() {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return (boolean) ReflectionHelper.invokeMethod("getCustomNameVisible", this.handle, nmsService.NMS_ENTITY_CLASS);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks if the entity has a custom name.
     * 
     * @return True if it is, false if not
     */
    public boolean hasCustomName() {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return (boolean) ReflectionHelper.invokeMethod("hasCustomName", this.handle, nmsService.NMS_ENTITY_CLASS);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets the passenger of the entity.
     * 
     * @return The passenger
     */
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
        protected BlockFace         blockFace         = null;
        protected float             headRotation      = Float.MIN_VALUE;
        protected EntityType        type              = null;
        protected Text              customName        = null;
        protected boolean           customNameVisible = false, invisible = false;

        protected Builder() {

        }

        /**
         * Sets the location of the builder.
         * 
         * @param location
         *            The location
         * @return The builder
         */
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
        public Builder facing(BlockFace blockFace) {
            this.blockFace = blockFace;

            return this;
        }

        /**
         * Sets the head rotation of the builder.
         * 
         * @param headRotation
         *            The head rotation
         * @return The builder
         */
        public Builder headRotation(float headRotation) {
            this.headRotation = headRotation;

            return this;
        }

        /**
         * Sets the entity type of the builder.
         * 
         * @param type
         *            The entity type
         * @return The builder
         */
        public Builder type(EntityType type) {
            this.type = type;

            return this;
        }

        /**
         * Sets the custom name of the builder.
         * 
         * @param customName
         *            The custom name
         * @return The builder
         */
        public Builder customName(Text customName) {
            this.customName = customName;

            return this;
        }

        /**
         * Sets whether or not the builder's custom name is visible.
         * 
         * @param customNameVisible
         *            Whether or not the custom name is visible
         * @return The builder
         */
        public Builder customNameVisible(boolean customNameVisible) {
            this.customNameVisible = customNameVisible;

            return this;
        }

        /**
         * Sets whether or not the custom name is visible.
         * 
         * @param invisible
         *            Whether or not the builder is invisible
         * @return The builder
         */
        public Builder invisible(boolean invisible) {
            this.invisible = invisible;

            return this;
        }

        /**
         * Creates and returns a new entity from the builder.
         * 
         * @return The entity
         */
        public FakeEntity build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            return new FakeEntity(this.type, this.location, this.blockFace, this.headRotation, this.customName != null ? this.customName.toLegacy() : null, this.customNameVisible, this.invisible, true);
        }
    }
}