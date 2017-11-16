package org.cantaloupe.entity;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.protocol.datawatcher.DataWatcher;
import org.cantaloupe.protocol.datawatcher.DataWatcherObject;
import org.cantaloupe.protocol.datawatcher.DataWatcherRegistry;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.skin.Skin;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.BitFlags;
import org.cantaloupe.util.MathUtils;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

/**
 * A class used to create a "fake" player entity.
 * 
 * @author Dylan Scheltens
 *
 */
public class FakePlayer extends FakeEntity {
    private UUID        uuid;
    private Text        displayName;
    private Skin        skin;
    private GameProfile gameProfile = null;

    private FakePlayer(ImmutableLocation location, BlockFace blockFace, UUID uuid, Text displayName, boolean invisible, Skin skin) {
        super(EntityType.PLAYER, location, blockFace, Float.MIN_VALUE, null, false, invisible, false);

        this.uuid = uuid;
        this.displayName = displayName;
        this.skin = skin;

        this.gameProfile = new GameProfile(uuid, displayName.toLegacy());

        if (skin != null && skin.getSignature() != null) {
            this.gameProfile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
        }

        this.create(invisible);
    }

    /**
     * Creates and returns a new builder.
     * 
     * @return The builder
     */
    public static Builder builder() {
        return new Builder();
    }

    private void create(boolean invisible) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object minecraftServer = service.NMS_MINECRAFTSERVER_CLASS.cast(ReflectionHelper.invokeMethod("getServer", service.BUKKIT_CRAFTSERVER_CLASS.cast(Bukkit.getServer())));
            Object world = service.NMS_WORLD_CLASS.cast(ReflectionHelper.invokeMethod("getHandle", service.BUKKIT_CRAFTWORLD_CLASS.cast(this.location.getWorld().toHandle())));
            Object playerInteractManager = service.NMS_PLAYERINTERACTMANAGER_CLASS.getConstructor(service.NMS_WORLD_CLASS).newInstance(world);
            Object entity = service.NMS_ENTITY_PLAYER_CLASS.getConstructor(service.NMS_MINECRAFTSERVER_CLASS, service.NMS_WORLDSERVER_CLASS, GameProfile.class, service.NMS_PLAYERINTERACTMANAGER_CLASS).newInstance(minecraftServer, world, this.gameProfile, playerInteractManager);

            ReflectionHelper.invokeMethod("setPositionRotation", entity, new Class<?>[] {
                    double.class, double.class, double.class, float.class, float.class
            }, this.location.getPosition().x, this.location.getPosition().y, this.location.getPosition().z, this.blockFace != null ? (this.blockFace != BlockFace.UP && this.blockFace != BlockFace.DOWN ? MathUtils.faceToYaw(this.blockFace) : 0f) : 0f,
                    this.blockFace != null ? (this.blockFace == BlockFace.UP ? 90f : this.blockFace == BlockFace.DOWN ? -90f : 0f) : this.location.getPitch());

            ReflectionHelper.invokeMethod("setInvisible", entity, new Class<?>[] {
                    boolean.class
            }, invisible);

            ReflectionHelper.setField("displayName", entity, "");

            this.handle = entity;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        FakeEntityContainer.addEntity(this);
    }

    @Override
    public void spawn(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = nmsService.NMS_PACKET_OUT_NAMEDENTITYSPAWN_CLASS.getConstructor(nmsService.NMS_ENTITY_HUMAN_CLASS).newInstance(this.handle);

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.updatePassenger(players, this.getPassenger());
        this.setHeadRotation(players,
                new Vector2f(this.blockFace != null ? (this.blockFace != BlockFace.UP && this.blockFace != BlockFace.DOWN ? MathUtils.faceToYaw(this.blockFace) : 0f) : 0f, this.blockFace != null ? (this.blockFace == BlockFace.UP ? -90f : this.blockFace == BlockFace.DOWN ? 90f : 0f) : this.location.getPitch()));
    }

    @Override
    public void despawn(Collection<Player> players) {
        super.despawn(players);
    }

    /**
     * Adds the entity to tab for an array of players.
     * 
     * @param players
     *            The array of players
     */
    public void addToTab(Player... players) {
        this.addToTab((Collection<Player>) Arrays.asList(players));
    }

    /**
     * Adds the entity to tab for a list of players.
     * 
     * @param players
     *            The list of players
     */
    public void addToTab(List<Player> players) {
        this.addToTab((Collection<Player>) players);
    }

    /**
     * Adds the entity to tab for a collection of players.
     * 
     * @param players
     *            The collection of players
     */
    public void addToTab(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(String.class, boolean.class, int.class).newInstance(this.gameProfile.getName(), true, 0);
            } else {
                Class<?> arrayClass = Class.forName("[L" + nmsService.NMS_ENTITY_PLAYER_CLASS.getName() + ";");
                Object array = Array.newInstance(nmsService.NMS_ENTITY_PLAYER_CLASS, 1);

                Array.set(array, 0, this.handle);

                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(nmsService.NMS_ENUM_PLAYERINFOACTION_CLASS, arrayClass).newInstance(ReflectionHelper.getStaticField("ADD_PLAYER", nmsService.NMS_ENUM_PLAYERINFOACTION_CLASS), array);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchFieldException | InstantiationException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the entity to tab for an array of players.
     * 
     * @param players
     *            The array of players
     */
    public void removeFromTab(Player... players) {
        this.removeFromTab((Collection<Player>) Arrays.asList(players));
    }

    /**
     * Removes the entity to tab for a list of players.
     * 
     * @param players
     *            The list of players
     */
    public void removeFromTab(List<Player> players) {
        this.removeFromTab((Collection<Player>) players);
    }

    /**
     * Removes the entity to tab for a collection of players.
     * 
     * @param players
     *            The collection of players
     */
    public void removeFromTab(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(String.class, boolean.class, int.class).newInstance(this.gameProfile.getName(), false, 0);
            } else {
                Class<?> arrayClass = Class.forName("[L" + nmsService.NMS_ENTITY_PLAYER_CLASS.getName() + ";");
                Object array = Array.newInstance(nmsService.NMS_ENTITY_PLAYER_CLASS, 1);

                Array.set(array, 0, this.handle);

                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(nmsService.NMS_ENUM_PLAYERINFOACTION_CLASS, arrayClass).newInstance(ReflectionHelper.getStaticField("REMOVE_PLAYER", nmsService.NMS_ENUM_PLAYERINFOACTION_CLASS), array);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets whether or not skin features are enabled for an array of players.
     * 
     * @param enabled
     *            Whether or not skin features are enabled
     * @param players
     *            The collection of players
     */
    public void setSkinFeaturesEnabled(boolean enabled, Player... players) {
        this.setSkinFeaturesEnabled((Collection<Player>) Arrays.asList(players), enabled);
    }

    /**
     * Sets whether or not skin features are enabled for an array of players.
     * 
     * @param enabled
     *            Whether or not skin features are enabled
     * @param players
     *            The collection of players
     */
    public void setSkinFeaturesEnabled(List<Player> players, boolean enabled) {
        this.setSkinFeaturesEnabled((Collection<Player>) players, enabled);
    }

    /**
     * Sets whether or not skin features are enabled for an array of players.
     * 
     * @param enabled
     *            Whether or not skin features are enabled
     * @param players
     *            The collection of players
     */
    public void setSkinFeaturesEnabled(Collection<Player> players, boolean enabled) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        byte flags = 0;

        if (enabled) {
            flags = BitFlags.setFlag(flags, (byte) 0x01);
            flags = BitFlags.setFlag(flags, (byte) 0x02);
            flags = BitFlags.setFlag(flags, (byte) 0x04);
            flags = BitFlags.setFlag(flags, (byte) 0x08);
            flags = BitFlags.setFlag(flags, (byte) 0x10);
            flags = BitFlags.setFlag(flags, (byte) 0x20);
            flags = BitFlags.setFlag(flags, (byte) 0x40);
        }

        try {
            DataWatcher dataWatcher = DataWatcher.of(null);
            dataWatcher.register(DataWatcherObject.<Byte>of(13, DataWatcherRegistry.BYTE), flags);

            Object packet = nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(this.getEntityID(), dataWatcher.toNMS(), true);
            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the tab list name for an array of players.
     * 
     * @param players
     *            The array of players
     */
    public void setTabListName(Text tabListName, Player... players) {
        this.setTabListName((Collection<Player>) Arrays.asList(players), tabListName);
    }

    /**
     * Updates the tab list name for a list of players.
     * 
     * @param players
     *            The list of players
     */
    public void setTabListName(List<Player> players, Text tabListName) {
        this.setTabListName((Collection<Player>) players, tabListName);
    }

    /**
     * Updates the tab list name for a collection of players.
     * 
     * @param players
     *            The collection of players
     */
    public void setTabListName(Collection<Player> players, Text tabListName) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            String newName = null;
            String currentName = (String) ReflectionHelper.invokeDeclaredMethod("getName", this.handle, nmsService.NMS_ENTITY_CLASS);

            if (tabListName == null) {
                newName = currentName;
            } else {
                newName = tabListName.toLegacy();
            }

            ReflectionHelper.setDeclaredField("listName", this.handle, newName.equals(currentName) ? null : ReflectionHelper.invokeStaticMethod("a", nmsService.NMS_CHATSERIALIZER_CLASS, "{\"text\":\"" + newName + "\"}"));
        } catch (IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(String.class, boolean.class, int.class).newInstance(this.gameProfile.getName(), false, 3);
            } else {
                Class<?> arrayClass = Class.forName("[L" + nmsService.NMS_ENTITY_PLAYER_CLASS.getName() + ";");
                Object array = Array.newInstance(nmsService.NMS_ENTITY_PLAYER_CLASS, 1);

                Array.set(array, 0, this.handle);

                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(nmsService.NMS_ENUM_PLAYERINFOACTION_CLASS, arrayClass).newInstance(ReflectionHelper.getStaticField("UPDATE_DISPLAY_NAME", nmsService.NMS_ENUM_PLAYERINFOACTION_CLASS), array);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void setHeadRotation(Collection<Player> players, float headRotation) {

    }

    /**
     * Sets the head rotation of the entity for an array of players
     * 
     * @param players
     *            The array of players
     * @param headRotation
     *            The head rotation
     */
    public void setHeadRotation(Vector2f rotation, Player... players) {
        this.setHeadRotation((Collection<Player>) Arrays.asList(players), rotation);
    }

    /**
     * Sets the head rotation of the entity for a list of players
     * 
     * @param players
     *            The list of players
     * @param headRotation
     *            The head rotation
     */
    public void setHeadRotation(List<Player> players, Vector2f rotation) {
        this.setHeadRotation((Collection<Player>) players, rotation);
    }

    /**
     * Sets the head rotation of the entity for a collection of players
     * 
     * @param players
     *            The collection of players
     * @param headRotation
     *            The head rotation
     */
    public void setHeadRotation(Collection<Player> players, Vector2f rotation) {
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
            Object headPacket = null;

            if (nmsService.getIntVersion() < 7) {
                headPacket = nmsService.NMS_PACKET_OUT_ENTITYHEADROTATION_CLASS.getConstructor(int.class, byte.class).newInstance(this.getEntityID(), this.getFixRotation(rotation.x));
            } else {
                headPacket = nmsService.NMS_PACKET_OUT_ENTITYHEADROTATION_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS, byte.class).newInstance(this.handle, this.getFixRotation(rotation.x));
            }

            for (Player player : players) {
                packetService.sendPacket(player, lookPacket);
                packetService.sendPacket(player, headPacket);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the UUID of the entity.
     * 
     * @return The UUID
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Gets the display name of the entity.
     * 
     * @return The display name
     */
    public Text getDisplayName() {
        return this.displayName;
    }

    public String getTabListName() {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return (String) ReflectionHelper.invokeDeclaredMethod("getName", this.handle, nmsService.NMS_ENTITY_CLASS);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the skin of the entity.
     * 
     * @return The skin
     */
    public Skin getSkin() {
        return this.skin;
    }

    /**
     * Gets the game profile of the entity.
     * 
     * @return The game profile
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public static final class Builder extends FakeEntity.Builder {
        private UUID uuid        = null;
        private Text displayName = null;
        private Skin skin        = null;

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
        public Builder headRotation(float headRotation) {
            return this;
        }

        @Deprecated
        public Builder type(EntityType type) {
            this.type = type;

            return this;
        }

        /**
         * Sets the UUID of the builder.
         * 
         * @param uuid
         *            The UUID
         * @return The builder
         */
        public Builder uuid(UUID uuid) {
            this.uuid = uuid;

            return this;
        }

        /**
         * Sets the display name of the builder.
         * 
         * @param displayName
         *            The display name
         * @return The builder
         */
        public Builder displayName(Text displayName) {
            this.displayName = displayName;

            return this;
        }

        /**
         * Sets the display name of the builder.
         * 
         * @param displayName
         *            The display name
         * @return The builder
         */
        public Builder displayName(String displayName) {
            this.displayName = Text.fromLegacy(displayName);

            return this;
        }

        @Deprecated
        public Builder customName(Text customName) {
            this.customName = customName;

            return this;
        }

        @Deprecated
        public Builder customNameVisible(boolean customNameVisible) {
            this.customNameVisible = customNameVisible;

            return this;
        }

        @Override
        public Builder invisible(boolean invisible) {
            this.invisible = invisible;

            return this;
        }

        /**
         * Sets the skin of the builder.
         * 
         * @param skin
         *            The skin
         * @return The builder
         */
        public Builder skin(Skin skin) {
            this.skin = skin;

            return this;
        }

        @Override
        public FakePlayer build() {
            if (this.location == null) {
                if (this.rotation != null) {
                    this.location = ImmutableLocation.of(this.world, this.position, this.rotation);
                } else {
                    this.location = ImmutableLocation.of(this.world, this.position);
                }
            }

            if (this.uuid == null) {
                this.uuid = UUID.randomUUID();
            }

            return new FakePlayer(this.location, this.blockFace, this.uuid, this.displayName, this.invisible, this.skin);
        }
    }
}