package org.cantaloupe.hologram;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector3d;

public class Hologram extends WorldObject {
    private ImmutableLocation  location;
    private List<Text>         lines;
    private final List<Object> entities;
    private final List<Object> spawnPackets;
    private Object             destroyPacket;
    private final List<Player> players;

    // Services
    private NMSService         nmsService    = null;
    private PacketService      packetService = null;

    private Hologram(ImmutableLocation location, List<Text> lines) {
        this.location = location;
        this.lines = lines;
        this.entities = new ArrayList<Object>();
        this.spawnPackets = new ArrayList<Object>();
        this.players = new ArrayList<Player>();

        // Services
        this.nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        this.packetService = Cantaloupe.getServiceManager().provide(PacketService.class);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void place() {
        this.location.getWorld().place(this);
    }

    public void remove() {
        this.location.getWorld().remove(this);
    }

    public void placeFor(Player player) {
        if (!this.isPlacedFor(player)) {
            for (Object packet : this.spawnPackets) {
                this.packetService.sendPacket(player, packet);
            }

            this.players.add(player);
        }
    }

    public void removeFor(Player player) {
        if (this.isPlacedFor(player)) {
            this.packetService.sendPacket(player, this.destroyPacket);
            this.players.remove(player);
        }
    }

    @Override
    public void tickFor(Player player) {
        if (player.getLocation().getPosition().distance(this.getLocation().getPosition()) <= 48) {
            this.placeFor(player);
        } else {
            this.removeFor(player);
        }
    }

    public void setPosition(Vector3d position) {
        this.location = ImmutableLocation.of(this.location.getWorld(), position);

        Object[] packets = new Object[this.entities.size()];
        int count = 0;
        for (Object entity : this.entities) {
            try {
                ReflectionHelper.invokeMethod("setPosition", entity, new Class<?>[] {
                        double.class, double.class, double.class
                }, position.x + 0.5, (position.y - 2.0) - (count * 0.25), position.z + 0.5);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

            try {
                packets[count] = this.nmsService.getNMSClass("PacketPlayOutEntityTeleport").getConstructor(this.nmsService.NMS_ENTITY_CLASS).newInstance(entity);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

            count++;
        }

        for (Player player : this.players) {
            for (Object packet : packets) {
                this.packetService.sendPacket(player, packet);
            }
        }
    }

    public void setLines(Text... lines) {
        Object[] packets = new Object[lines.length];
        for (int i = 0; i < lines.length; i++) {
            if(!lines[i].toLegacy().equalsIgnoreCase(this.lines.get(i).toLegacy())) {
                Object entity = this.entities.get(i);

                try {
                    Object dataWatcher = this.nmsService.NMS_DATAWATCHER_CLASS.getConstructor(this.nmsService.NMS_ENTITY_CLASS).newInstance(new Object[] {
                            null
                    });
                    
                    ReflectionHelper.invokeMethod("register", dataWatcher, new Class<?>[] {
                        this.nmsService.NMS_DATAWATCHEROBJECT_CLASS, Object.class
                    }, this.nmsService.NMS_DATAWATCHEROBJECT_CLASS.getConstructor(int.class, this.nmsService.NMS_DATAWATCHERSERIALIZER_CLASS).newInstance(2, ReflectionHelper.getStaticField("d", this.nmsService.NMS_DATAWATCHERREGISTRY_CLASS)), lines[i].toLegacy());

                    packets[i] = this.nmsService.NMS_PACKET_OUT_ENTITYMETA_CLASS.getConstructor(int.class, this.nmsService.NMS_DATAWATCHER_CLASS, boolean.class).newInstance(ReflectionHelper.invokeMethod("getId", entity), dataWatcher, true);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        
        for (Player player : this.players) {
            for (Object packet : packets) {
                this.packetService.sendPacket(player, packet);
            }
        }
        
        this.lines = Arrays.asList(lines);
    }

    @Override
    protected void onPlaced() {
        for (Player player : this.location.getWorld().getPlayers()) {
            this.tickFor(player);
            this.players.add(player);
        }
    }

    @Override
    protected void onRemoved() {
        for (Player player : this.players) {
            this.packetService.sendPacket(player, this.destroyPacket);
        }

        this.players.clear();
    }

    private void create() {
        int count = 0;
        int[] entityIDs = new int[this.lines.size()];

        for (Text line : this.lines) {
            try {
                Object craftWorld = this.nmsService.BUKKIT_CRAFTWORLD_CLASS.cast(this.location.getWorld().toHandle());
                Object handle = craftWorld.getClass().getDeclaredMethod("getHandle").invoke(craftWorld);
                Object entity = this.nmsService.NMS_ENTITY_ARMORSTAND_CLASS.getConstructor(this.nmsService.NMS_WORLD_CLASS, double.class, double.class, double.class).newInstance(handle, this.location.getPosition().x + 0.5,
                        (this.location.getPosition().y - 2.0) - (count * 0.25), this.location.getPosition().z + 0.5);

                ReflectionHelper.invokeMethod("setCustomName", entity, line.toLegacy());
                ReflectionHelper.invokeMethod("setCustomNameVisible", entity, new Class<?>[] {
                        boolean.class
                }, true);

                ReflectionHelper.invokeMethod("setInvisible", entity, new Class<?>[] {
                        boolean.class
                }, true);

                ReflectionHelper.invokeMethod(this.nmsService.getIntVersion() < 11 ? "setGravity" : "setNoGravity", entity, new Class<?>[] {
                        boolean.class
                }, this.nmsService.getIntVersion() < 11 ? false : true);

                this.entities.add(entity);
            } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            count++;
        }

        for (int i = 0; i < this.entities.size(); i++) {
            try {
                this.spawnPackets.add(this.nmsService.NMS_PACKET_OUT_SPAWNENTITYLIVING_CLASS.getConstructor(this.nmsService.NMS_ENTITY_LIVING_CLASS).newInstance(this.entities.get(i)));

                entityIDs[i] = (int) ReflectionHelper.invokeMethod("getId", this.entities.get(i));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        try {
            this.destroyPacket = this.nmsService.NMS_PACKET_OUT_DESTROYENTITY_CLASS.getConstructor(int[].class).newInstance(entityIDs);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlacedFor(Player player) {
        return this.players.contains(player);
    }

    @Override
    public ImmutableLocation getLocation() {
        return this.location;
    }

    public List<Text> getLines() {
        return this.lines;
    }

    public static final class Builder {
        private Vector3d          position = null;
        private World             world    = null;
        private ImmutableLocation location = null;
        private List<Text>        lines    = null;

        private Builder() {
            this.lines = new ArrayList<Text>();
        }

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

        public Builder line(Text line) {
            this.lines.add(line);

            return this;
        }

        public Builder lines(Text... lines) {
            this.lines.addAll(Arrays.asList(lines));

            return this;
        }

        public Builder lines(List<Text> lines) {
            this.lines.addAll(lines);

            return this;
        }

        public Builder lines(Collection<Text> lines) {
            this.lines.addAll(lines);

            return this;
        }

        public Hologram build() {
            if (this.location == null) {
                this.location = ImmutableLocation.of(this.world, this.position);
            }

            Hologram hologram = new Hologram(this.location, this.lines);
            hologram.create();

            return hologram;
        }
    }
}