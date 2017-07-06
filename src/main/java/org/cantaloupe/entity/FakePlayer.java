package org.cantaloupe.entity;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector3d;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class FakePlayer extends FakeEntity {
    private final UUID   uuid;
    private final String name;
    private GameProfile  gameProfile = null;

    private FakePlayer(ImmutableLocation location, UUID uuid, String name, boolean invisible) {
        super(EntityType.PLAYER, location, null, false, invisible, false);

        this.uuid = uuid;
        this.name = name;
        this.gameProfile = new GameProfile(uuid, name);

        String value = "eyJ0aW1lc3RhbXAiOjE0NDI4MzY1MTU1NzksInByb2ZpbGVJZCI6IjkwZWQ3YWY0NmU4YzRkNTQ4MjRkZTc0YzI1MTljNjU1IiwicHJvZmlsZU5hbWUiOiJDb25DcmFmdGVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMWNlZDMzMjNmYjczMmFjMTc3MTc5Yjg5NWQ5YzJmNjFjNzczZWYxNTVlYmQ1Y2M4YzM5NTZiZjlhMDlkMTIifX19";
        String signature = "tFGNBQNpxNGvD27SN7fqh3LqNinjJJFidcdF8LTRHOdoMNXcE5ezN172BnDlRsExspE9X4z7FPglqh/b9jrLFDfQrdqX3dGm1cKjYbvOXL9BO2WIOEJLTDCgUQJC4/n/3PZHEG2mVADc4v125MFYMfjzkznkA6zbs7w6z8f7pny9eCWNXPOQklstcdc1h/LvflnR+E4TUuxCf0jVsdT5AZsUYIsJa6fvr0+vItUXUdQ3pps0zthObPEnBdLYMtNY3G6ZLGVKcSGa/KRK2D/k69fmu/uTKbjAWtniFB/sdO0VNhLuvyr/PcZVXB78l1SfBR88ZMiW6XSaVqNnSP+MEfRkxgkJWUG+aiRRLE8G5083EQ8vhIle5GxzK68ZR48IrEX/JwFjALslCLXAGR05KrtuTD3xyq2Nut12GCaooBEhb46sipWLq4AXI9IpJORLOW8+GvY+FcDwMqXYN94juDQtbJGCQo8PX670YjbmVx7+IeFjLJJTZotemXu1wiQmDmtAAmug4U5jgMYIJryXMitD7r5pEop/cw42JbCO2u0b5NB7sI/mr4OhBKEesyC5usiARzuk6e/4aJUvwQ9nsiXfeYxZz8L/mh6e8YPJMyhVkFtblbt/4jPe0bs3xSUXO9XrDyhy9INC0jlLT22QjNzrDkD8aiGAopVvfnTTAug=";
        this.gameProfile.getProperties().put("textures", new Property("textures", value, signature));

        this.create(uuid, name, invisible);
    }

    public static Builder builder() {
        return new Builder();
    }

    private void create(UUID uuid, String name, boolean invisible) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object minecraftServer = service.NMS_MINECRAFTSERVER_CLASS.cast(ReflectionHelper.invokeMethod("getServer", service.BUKKIT_CRAFTSERVER_CLASS.cast(Bukkit.getServer())));
            Object world = service.NMS_WORLD_CLASS.cast(ReflectionHelper.invokeMethod("getHandle", service.BUKKIT_CRAFTWORLD_CLASS.cast(this.location.getWorld().toHandle())));
            Object playerInteractManager = service.NMS_PLAYERINTERACTMANAGER_CLASS.getConstructor(service.NMS_WORLD_CLASS).newInstance(world);
            Object entity = service.NMS_ENTITY_PLAYER_CLASS.getConstructor(service.NMS_MINECRAFTSERVER_CLASS, service.NMS_WORLDSERVER_CLASS, GameProfile.class, service.NMS_PLAYERINTERACTMANAGER_CLASS).newInstance(minecraftServer, world, this.gameProfile,
                    playerInteractManager);

            ReflectionHelper.invokeMethod("setPositionRotation", entity, new Class<?>[] {
                    double.class, double.class, double.class, float.class, float.class
            }, this.location.getPosition().x, this.location.getPosition().y, this.location.getPosition().z, this.location.getYaw(), this.location.getPitch());

            ReflectionHelper.invokeMethod("setInvisible", entity, new Class<?>[] {
                    boolean.class
            }, invisible);

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
            Object packet = nmsService.NMS_PACKET_OUT_NAMEDENTITYSPAWN_CLASS.getConstructor(nmsService.NMS_ENTITY_HUMAN_CLASS).newInstance(this.handle);

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.updatePassenger(players, this.getPassenger());
    }

    @Override
    public void despawn(Collection<Player> players) {
        super.despawn(players);
    }

    public void addToTab(Player... players) {
        this.addToTab((Collection<Player>) Arrays.asList(players));
    }

    public void addToTab(List<Player> players) {
        this.addToTab((Collection<Player>) players);
    }

    public void addToTab(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(String.class, boolean.class, int.class).newInstance(this.name, true, 0);
            } else {
                Class<?> arrayClass = Class.forName("[L" + nmsService.NMS_ENTITY_PLAYER_CLASS.getName() + ";");
                Object array = Array.newInstance(nmsService.NMS_ENTITY_PLAYER_CLASS, 1);

                Array.set(array, 0, this.handle);

                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(nmsService.NMS_ENUM_PLAYERINFOACTION_CLASS, arrayClass).newInstance(ReflectionHelper.getStaticField("ADD_PLAYER", nmsService.NMS_ENUM_PLAYERINFOACTION_CLASS), array);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalArgumentException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeFromTab(Player... players) {
        this.removeFromTab((Collection<Player>) Arrays.asList(players));
    }

    public void removeFromTab(List<Player> players) {
        this.removeFromTab((Collection<Player>) players);
    }

    public void removeFromTab(Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_PLAYERINFO_CLASS.getConstructor(String.class, boolean.class, int.class).newInstance(this.name, false, 0);
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

    @Override
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
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.location = ImmutableLocation.of(this.location.getWorld(), position);
    }

    @Override
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
            Object packet = null;

            if (nmsService.getIntVersion() < 7) {
                packet = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT.newInstance();

                ReflectionHelper.setDeclaredField("a", packet, this.getEntityID());
                ReflectionHelper.setDeclaredField("b", packet, this.getFixPosition(location.getPosition().x));
                ReflectionHelper.setDeclaredField("c", packet, this.getFixPosition(location.getPosition().y));
                ReflectionHelper.setDeclaredField("d", packet, this.getFixPosition(location.getPosition().z));
                ReflectionHelper.setDeclaredField("e", packet, this.getFixRotation(location.getYaw()));
                ReflectionHelper.setDeclaredField("f", packet, this.getFixRotation(location.getPitch()));
            } else {
                packet = nmsService.NMS_PACKET_OUT_ENTITYTELEPORT.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(this.handle);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.location = location;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public static final class Builder extends FakeEntity.Builder {
        private UUID   uuid = null;
        private String name = null;

        public Builder location(ImmutableLocation location) {
            this.location = location;

            return this;
        }

        public Builder position(Vector3d position) {
            this.position = position;

            return this;
        }

        public Builder world(World world) {
            this.world = world;

            return this;
        }

        @Deprecated
        public Builder type(EntityType type) {
            this.type = type;

            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;

            return this;
        }

        public Builder name(String name) {
            this.name = name;

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

        public Builder invisible(boolean invisible) {
            this.invisible = invisible;

            return this;
        }

        public FakePlayer build() {
            if (this.location == null) {
                this.location = ImmutableLocation.of(this.world, this.position);
            }

            return new FakePlayer(this.location, this.uuid, this.name, this.invisible);
        }
    }
}