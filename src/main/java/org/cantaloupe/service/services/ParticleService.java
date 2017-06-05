package org.cantaloupe.service.services;

import java.util.Arrays;
import java.util.List;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.Service;
import org.cantaloupe.user.User;
import org.cantaloupe.world.location.Location;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class ParticleService implements Service {
    private PacketService packetService = null;

    @Override
    public void load() {
        this.packetService = Cantaloupe.getServiceManager().getService(PacketService.class);
    }

    @Override
    public void unload() {
        this.packetService = null;
    }

    public void display(User user, ParticleType type, Vector3d position, Vector3f offset, boolean longDistance, float speed, int amount) {
        this.packetService.sendParticlePacket(user, type, position, offset, longDistance, speed, amount);
    }

    public void display(User user, ParticleType type, Location location, Vector3f offset, boolean longDistance, float speed, int amount) {
        this.packetService.sendParticlePacket(user, type, new Vector3d(location.getPosition().x, location.getPosition().y, location.getPosition().z), offset, longDistance, speed, amount);
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
        MOB_APPEARANCE("mobappearance", 41, 8);

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
}