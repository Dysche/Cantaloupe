package org.cantaloupe.service.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.math.color.ColorRGB;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.IService;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.location.Location;
import org.joml.Vector3f;

public class ParticleService implements IService {
    private NMSService nmsService         = null;
    private Enum<?>[]  enumParticleValues = null;

    @Override
    public void load() {
        this.nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        if (this.nmsService.getIntVersion() > 7) {
            try {
                this.enumParticleValues = (Enum<?>[]) this.nmsService.NMS_ENUM_PARTICLE_CLASS.getDeclaredMethod("values").invoke(null);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unload() {
        this.nmsService = null;
        this.enumParticleValues = null;
    }

    // Normal (Offset)
    public void display(ParticleType type, Location location, Vector3f offset, float range, int amount, Collection<Player> players) {
        this.display(type, location, offset, range > 256f, 0f, amount, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, Location location, Vector3f offset, float range, int amount, Player... players) {
        this.display(type, location, offset, range > 256f, 0f, amount, players);
    }

    public void display(ParticleType type, Location location, float range, int amount, Collection<Player> players) {
        this.display(type, location, new Vector3f(), range > 256f, 0f, amount, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, Location location, float range, int amount, Player... players) {
        this.display(type, location, new Vector3f(), range > 256f, 0f, amount, players);
    }

    public void display(ParticleType type, Location location, Vector3f offset, float range, Collection<Player> players) {
        this.display(type, location, offset, range > 256f, 0f, 1, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, Location location, Vector3f offset, float range, Player... players) {
        this.display(type, location, offset, range > 256f, 0f, 1, players);
    }

    public void display(ParticleType type, Location location, float range, Collection<Player> players) {
        this.display(type, location, new Vector3f(), range > 256f, 0f, 1, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, Location location, float range, Player... players) {
        this.display(type, location, new Vector3f(), range > 256f, 0f, 1, players);
    }

    public void display(ParticleType type, Location location, Vector3f offset, int amount, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, location, offset, this.isLongDistance(location, playerArray), 0f, amount, playerArray);
    }

    public void display(ParticleType type, Location location, Vector3f offset, int amount, Player... players) {
        this.display(type, location, offset, this.isLongDistance(location, players), 0f, amount, players);
    }

    public void display(ParticleType type, Location location, int amount, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, location, new Vector3f(), this.isLongDistance(location, playerArray), 0f, amount, playerArray);
    }

    public void display(ParticleType type, Location location, int amount, Player... players) {
        this.display(type, location, new Vector3f(), this.isLongDistance(location, players), 0f, amount, players);
    }

    public void display(ParticleType type, Location location, Vector3f offset, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, location, offset, this.isLongDistance(location, playerArray), 0f, 1, playerArray);
    }

    public void display(ParticleType type, Location location, Vector3f offset, Player... players) {
        this.display(type, location, offset, this.isLongDistance(location, players), 0f, 1, players);
    }

    public void display(ParticleType type, Location location, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, location, new Vector3f(), this.isLongDistance(location, playerArray), 0f, 1, playerArray);
    }

    public void display(ParticleType type, Location location, Player... players) {
        this.display(type, location, new Vector3f(), this.isLongDistance(location, players), 0f, 1, players);
    }

    public void display(ParticleType type, Location location, Vector3f offset, boolean longDistance, float speed, int amount, Collection<Player> players) {
        this.display(type, location, offset, longDistance, speed, amount, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, Location location, Vector3f offset, boolean longDistance, float speed, int amount, Player... players) {
        if (!this.isSupported(type)) {
            throw new ParticleVersionException("This particle effect isn't supported by your server version.");
        }

        if (type.hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data.");
        }

        if (type.hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(location)) {
            throw new IllegalArgumentException("This particle effect requires water at its location.");
        }

        this.sendParticlePacket(players, type, null, location, offset, longDistance, speed, amount);
    }

    // Data
    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, float range, int amount, Collection<Player> players) {
        this.display(type, data, location, offset, range > 256f, 0f, amount, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, float range, int amount, Player... players) {
        this.display(type, data, location, offset, range > 256f, 0f, amount, players);
    }

    public void display(ParticleType type, ParticleData data, Location location, float range, int amount, Collection<Player> players) {
        this.display(type, data, location, new Vector3f(), range > 256f, 0f, amount, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, ParticleData data, Location location, float range, int amount, Player... players) {
        this.display(type, data, location, new Vector3f(), range > 256f, 0f, amount, players);
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, float range, Collection<Player> players) {
        this.display(type, data, location, offset, range > 256f, 0f, 1, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, float range, Player... players) {
        this.display(type, data, location, offset, range > 256f, 0f, 1, players);
    }

    public void display(ParticleType type, ParticleData data, Location location, float range, Collection<Player> players) {
        this.display(type, data, location, new Vector3f(), range > 256f, 0f, 1, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, ParticleData data, Location location, float range, Player... players) {
        this.display(type, data, location, new Vector3f(), range > 256f, 0f, 1, players);
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, int amount, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, data, location, offset, this.isLongDistance(location, playerArray), 0f, amount, playerArray);
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, int amount, Player... players) {
        this.display(type, data, location, offset, this.isLongDistance(location, players), 0f, amount, players);
    }

    public void display(ParticleType type, ParticleData data, Location location, int amount, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, data, location, new Vector3f(), this.isLongDistance(location, playerArray), 0f, amount, playerArray);
    }

    public void display(ParticleType type, ParticleData data, Location location, int amount, Player... players) {
        this.display(type, data, location, new Vector3f(), this.isLongDistance(location, players), 0f, amount, players);
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, data, location, offset, this.isLongDistance(location, playerArray), 0f, 1, playerArray);
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, Player... players) {
        this.display(type, data, location, offset, this.isLongDistance(location, players), 0f, 1, players);
    }

    public void display(ParticleType type, ParticleData data, Location location, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, data, location, new Vector3f(), this.isLongDistance(location, playerArray), 0f, 1, playerArray);
    }

    public void display(ParticleType type, ParticleData data, Location location, Player... players) {
        this.display(type, data, location, new Vector3f(), this.isLongDistance(location, players), 0f, 1, players);
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, boolean longDistance, float speed, int amount, Collection<Player> players) {
        this.display(type, data, location, offset, longDistance, speed, amount, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, ParticleData data, Location location, Vector3f offset, boolean longDistance, float speed, int amount, Player... players) {
        if (!this.isSupported(type)) {
            throw new ParticleVersionException("This particle effect isn't supported by your server version.");
        }

        if (!type.hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data.");
        }

        if (!this.isDataCorrect(type, data)) {
            throw new ParticleDataException("The particle effect's data type is incorrect.");
        }

        this.sendParticlePacket(players, type, data, location, offset, longDistance, speed, amount);
    }

    // Color
    public void display(ParticleType type, Location location, ColorRGB color, float range, Collection<Player> players) {
        this.display(type, location, color, range > 256f, 1f, 0, players.toArray(new Player[0]));
    }

    public void display(ParticleType type, Location location, ColorRGB color, float range, Player... players) {
        this.display(type, location, color, range > 256f, 1f, 0, players);
    }

    public void display(ParticleType type, Location location, ColorRGB color, Collection<Player> players) {
        Player[] playerArray = players.toArray(new Player[0]);

        this.display(type, location, color, this.isLongDistance(location, playerArray), 1f, 0, playerArray);
    }

    public void display(ParticleType type, Location location, ColorRGB color, Player... players) {
        this.display(type, location, color, this.isLongDistance(location, players), 1f, 0, players);
    }

    private void display(ParticleType type, Location location, ColorRGB color, boolean longDistance, float speed, int amount, Player... players) {
        if (!this.isSupported(type)) {
            throw new ParticleVersionException("This particle effect isn't supported by your server version.");
        }

        if (type.hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data.");
        }

        if (type.hasProperty(ParticleProperty.REQUIRES_WATER) && !this.isWater(location)) {
            throw new IllegalArgumentException("This particle effect requires water at its location.");
        }

        if (!type.hasProperty(ParticleProperty.COLORABLE)) {
            throw new IllegalArgumentException("This particle effect isn't colorable.");
        }

        this.sendParticlePacket(players, type, null, location, color.toVector(), longDistance, speed, amount);
    }

    private boolean isLongDistance(Location location, Player[] players) {
        String world = location.getWorld().getName();

        for (Player player : players) {
            Location playerLocation = player.getLocation();

            if (!world.equals(playerLocation.getWorld().getName()) || playerLocation.toHandle().distanceSquared(location.toHandle()) < 65536) {
                continue;
            }

            return true;
        }

        return false;
    }

    private boolean isDataCorrect(ParticleType effect, ParticleData data) {
        return ((effect == ParticleType.BLOCK_CRACK || effect == ParticleType.BLOCK_DUST) && data instanceof BlockData) || (effect == ParticleType.ITEM_CRACK && data instanceof ItemData);
    }

    private boolean isWater(Location location) {
        Material material = location.getBlock().getType();

        return material == Material.WATER || material == Material.STATIONARY_WATER;
    }

    private boolean isSupported(ParticleType type) {
        return type.getRequiredVersion() <= this.nmsService.getIntVersion();
    }

    // Packet
    protected void sendParticlePacket(Player[] players, ParticleType type, ParticleData data, Location location, Vector3f offset, boolean longDistance, float speed, int amount) {
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
            Object packet = this.nmsService.NMS_PACKET_OUT_WORLDPARTICLES_CLASS.newInstance();

            if (this.nmsService.getIntVersion() < 7) {
                String name = type.getName();

                if (data != null) {
                    name += data.getPacketDataString();
                }

                ReflectionHelper.setDeclaredField("a", packet, name);
            } else {
                ReflectionHelper.setDeclaredField("a", packet, this.enumParticleValues[type.getID()]);
                ReflectionHelper.setDeclaredField("j", packet, longDistance);

                if (data != null) {
                    int[] packetData = data.getPacketData();

                    ReflectionHelper.setDeclaredField("k", packet, type == ParticleType.ITEM_CRACK ? packetData : new int[] {
                            packetData[0] | (packetData[1] << 12)
                    });
                }
            }

            ReflectionHelper.setDeclaredField("b", packet, (float) location.getPosition().x);
            ReflectionHelper.setDeclaredField("c", packet, (float) location.getPosition().y);
            ReflectionHelper.setDeclaredField("d", packet, (float) location.getPosition().z);
            ReflectionHelper.setDeclaredField("e", packet, offsetX);
            ReflectionHelper.setDeclaredField("f", packet, offsetY);
            ReflectionHelper.setDeclaredField("g", packet, offsetZ);
            ReflectionHelper.setDeclaredField("h", packet, speed);
            ReflectionHelper.setDeclaredField("i", packet, amount);

            for (Player player : players) {
                player.sendPacket(packet);
            }
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "particle";
    }

    public enum ParticleType {
        // Values
        EXPLOSION_NORMAL("explode", 0, -1, ParticleProperty.DIRECTIONAL),
        EXPLOSION_LARGE("largeexplode", 1, -1),
        EXPLOSION_HUGE("hugeexplosion", 2, -1),
        FIREWORKS_SPARK("fireworksSpark", 3, -1, ParticleProperty.DIRECTIONAL),
        WATER_BUBBLE("bubble", 4, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_WATER),
        WATER_SPLASH("splash", 5, -1, ParticleProperty.DIRECTIONAL),
        WATER_WAKE("wake", 6, 7, ParticleProperty.DIRECTIONAL),
        SUSPENDED("suspended", 7, -1, ParticleProperty.REQUIRES_WATER),
        SUSPENDED_DEPTH("depthSuspend", 8, -1, ParticleProperty.DIRECTIONAL),
        CRIT("crit", 9, -1, ParticleProperty.DIRECTIONAL),
        CRIT_MAGIC("magicCrit", 10, -1, ParticleProperty.DIRECTIONAL),
        SMOKE_NORMAL("smoke", 11, -1, ParticleProperty.DIRECTIONAL),
        SMOKE_LARGE("largesmoke", 12, -1, ParticleProperty.DIRECTIONAL),
        SPELL("spell", 13, -1),
        SPELL_INSTANT("instantSpell", 14, -1),
        SPELL_MOB("mobSpell", 15, -1, ParticleProperty.COLORABLE),
        SPELL_MOB_AMBIENT("mobSpellAmbient", 16, -1, ParticleProperty.COLORABLE),
        SPELL_WITCH("witchMagic", 17, -1),
        DRIP_WATER("dripWater", 18, -1),
        DRIP_LAVA("dripLava", 19, -1),
        VILLAGER_ANGRY("angryVillager", 20, -1),
        VILLAGER_HAPPY("happyVillager", 21, -1, ParticleProperty.DIRECTIONAL),
        TOWN_AURA("townaura", 22, -1, ParticleProperty.DIRECTIONAL),
        NOTE("note", 23, -1, ParticleProperty.COLORABLE),
        PORTAL("portal", 24, -1, ParticleProperty.DIRECTIONAL),
        ENCHANTMENT_TABLE("enchantmenttable", 25, -1, ParticleProperty.DIRECTIONAL),
        FLAME("flame", 26, -1, ParticleProperty.DIRECTIONAL),
        LAVA("lava", 27, -1),
        FOOTSTEP("footstep", 28, -1),
        CLOUD("cloud", 29, -1, ParticleProperty.DIRECTIONAL),
        REDSTONE("reddust", 30, -1, ParticleProperty.COLORABLE),
        SNOWBALL("snowballpoof", 31, -1),
        SNOW_SHOVEL("snowshovel", 32, -1, ParticleProperty.DIRECTIONAL),
        SLIME("slime", 33, -1),
        HEART("heart", 34, -1),
        BARRIER("barrier", 35, 8),
        ITEM_CRACK("iconcrack", 36, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
        BLOCK_CRACK("blockcrack", 37, -1, ParticleProperty.REQUIRES_DATA),
        BLOCK_DUST("blockdust", 38, 7, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
        WATER_DROP("droplet", 39, 8),
        ITEM_TAKE("take", 40, 8),
        MOB_APPEARANCE("mobappearance", 41, 8),
        DRAGON_BREATH("dragonbreath", 42, 9),
        END_ROD("endRod", 43, 9),
        DAMAGE_INDICATOR("damageIndicator", 44, 9),
        SWEEP_ATTACK("sweepAttack", 45, 9),
        FALLING_DUST("fallingdust", 46, 10, ParticleProperty.REQUIRES_WATER),
        TOTEM("totem", 47, 11),
        SPIT("spit", 48, 11);

        // Enum Body
        private final String                 type;
        private final int                    ID;
        private final int                    requiredVersion;
        private final List<ParticleProperty> properties;

        private ParticleType(String type, int ID, int requiredVersion, ParticleProperty... properties) {
            this.type = type;
            this.ID = ID;
            this.requiredVersion = requiredVersion;
            this.properties = Arrays.asList(properties);
        }

        public boolean hasProperty(ParticleProperty property) {
            return this.properties.contains(property);
        }

        public String getName() {
            return this.type;
        }

        public int getID() {
            return this.ID;
        }

        public int getRequiredVersion() {
            return this.requiredVersion;
        }
    }

    public enum ParticleProperty {
        COLORABLE, DIRECTIONAL, REQUIRES_WATER, REQUIRES_DATA
    }

    public static abstract class ParticleData {
        private final Material material;
        private final byte     data;
        private final int[]    packetData;

        @SuppressWarnings("deprecation")
        public ParticleData(Material material, byte data) {
            this.material = material;
            this.data = data;
            this.packetData = new int[] {
                    material.getId(), data
            };
        }

        public Material getMaterial() {
            return material;
        }

        public byte getData() {
            return data;
        }

        public int[] getPacketData() {
            return packetData;
        }

        public String getPacketDataString() {
            return "_" + packetData[0] + "_" + packetData[1];
        }
    }

    public static final class ItemData extends ParticleData {
        private ItemData(Material material, byte data) {
            super(material, data);
        }

        public static ItemData of(Material material, byte data) {
            return new ItemData(material, data);
        }

        public static ItemData of(Material material) {
            return new ItemData(material, (byte) 0);
        }
    }

    public static final class BlockData extends ParticleData {
        private BlockData(Material material, byte data) throws IllegalArgumentException {
            super(material, data);

            if (!material.isBlock()) {
                throw new IllegalArgumentException("The material has to be a block.");
            }
        }

        public static BlockData of(Material material, byte data) {
            return new BlockData(material, data);
        }

        public static BlockData of(Material material) {
            return new BlockData(material, (byte) 0);
        }
    }

    private final class ParticleDataException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        public ParticleDataException(String message) {
            super(message);
        }
    }

    private final class ParticleVersionException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        public ParticleVersionException(String message) {
            super(message);
        }
    }
}