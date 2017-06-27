package org.cantaloupe.service.services;

import org.bukkit.Bukkit;
import org.cantaloupe.service.Service;

public class NMSService implements Service {
    private String  serverPackage                          = null;
    private String  bukkitPackage                          = null;

    private String  serverVersion                          = null;
    private int     intVersion                             = -1;

    // NMS Classes
    public Class<?> NMS_WORLD_CLASS                        = null;
    public Class<?> NMS_WORLD_SERVER_CLASS                 = null;
    public Class<?> NMS_PACKET_CLASS                       = null;
    public Class<?> NMS_PACKET_OUT_SPAWNENTITYLIVING_CLASS = null;
    public Class<?> NMS_PACKET_OUT_DESTROYENTITY_CLASS     = null;
    public Class<?> NMS_PACKET_OUT_WORLDPARTICLES_CLASS    = null;
    public Class<?> NMS_PACKET_OUT_ENTITYMETA_CLASS        = null;
    public Class<?> NMS_ENTITY_CLASS                       = null;
    public Class<?> NMS_ENTITY_LIVING_CLASS                = null;
    public Class<?> NMS_ENTITY_ARMORSTAND_CLASS            = null;
    public Class<?> NMS_ENUM_PARTICLE_CLASS                = null;
    public Class<?> NMS_DATAWATCHER_CLASS                  = null;
    public Class<?> NMS_DATAWATCHEROBJECT_CLASS            = null;
    public Class<?> NMS_DATAWATCHERREGISTRY_CLASS          = null;
    public Class<?> NMS_DATAWATCHERSERIALIZER_CLASS        = null;

    // Bukkit Classes
    public Class<?> BUKKIT_CRAFTWORLD_CLASS                = null;
    public Class<?> BUKKIT_CRAFTPLAYER_CLASS               = null;

    @Override
    public void load() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        this.serverVersion = version.substring(version.lastIndexOf('.') + 1);
        this.intVersion = Integer.parseInt(this.getServerVersion().split("_")[1]);

        this.serverPackage = "net.minecraft.server." + this.getServerVersion() + ".";
        this.bukkitPackage = "org.bukkit.craftbukkit." + this.getServerVersion() + ".";

        this.loadClasses();
    }

    private void loadClasses() {
        // NMS
        this.NMS_WORLD_CLASS = this.getNMSClass("World");
        this.NMS_WORLD_SERVER_CLASS = this.getNMSClass("WorldServer");
        this.NMS_PACKET_CLASS = this.getNMSClass("Packet");
        this.NMS_PACKET_OUT_SPAWNENTITYLIVING_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet24MobSpawn" : "PacketPlayOutSpawnEntityLiving");
        this.NMS_PACKET_OUT_DESTROYENTITY_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet29DestroyEntity" : "PacketPlayOutEntityDestroy");
        this.NMS_PACKET_OUT_WORLDPARTICLES_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
        this.NMS_PACKET_OUT_ENTITYMETA_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet40EntityMetadata" : "PacketPlayOutEntityMetadata");
        this.NMS_ENTITY_CLASS = this.getNMSClass("Entity");
        this.NMS_ENTITY_LIVING_CLASS = this.getNMSClass("EntityLiving");
        this.NMS_ENTITY_ARMORSTAND_CLASS = this.getNMSClass("EntityArmorStand");
        this.NMS_ENUM_PARTICLE_CLASS = this.getNMSClass("EnumParticle");
        this.NMS_DATAWATCHER_CLASS = this.getNMSClass("DataWatcher");
        this.NMS_DATAWATCHEROBJECT_CLASS = this.getNMSClass("DataWatcherObject");
        this.NMS_DATAWATCHERREGISTRY_CLASS = this.getNMSClass("DataWatcherRegistry");
        this.NMS_DATAWATCHERSERIALIZER_CLASS = this.getNMSClass("DataWatcherSerializer");

        // Bukkit
        this.BUKKIT_CRAFTWORLD_CLASS = this.getBukkitClass("CraftWorld");
        this.BUKKIT_CRAFTPLAYER_CLASS = this.getBukkitClass("entity.CraftPlayer");
    }

    @Override
    public void unload() {
        this.serverPackage = null;
        this.bukkitPackage = null;
        this.serverVersion = null;
        this.intVersion = -1;
    }

    public String getServerVersion() {
        return this.serverVersion;
    }

    public int getIntVersion() {
        return this.intVersion;
    }

    public String getServerPackage() {
        return this.serverPackage;
    }

    public String getBukkitPackage() {
        return this.bukkitPackage;
    }

    public Class<?> getNMSClass(String path) {
        try {
            return Class.forName(this.serverPackage + path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Class<?> getBukkitClass(String path) {
        try {
            return Class.forName(this.bukkitPackage + path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName() {
        return "nms";
    }
}