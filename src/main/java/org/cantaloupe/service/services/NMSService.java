package org.cantaloupe.service.services;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.cantaloupe.inventory.ItemStack;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.Service;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;

public class NMSService implements Service {
    private String  serverPackage                          = null;
    private String  bukkitPackage                          = null;

    private String  serverVersion                          = null;
    private int     intVersion                             = -1;

    // NMS Classes
    public Class<?> NMS_MINECRAFTSERVER_CLASS              = null;
    public Class<?> NMS_WORLD_CLASS                        = null;
    public Class<?> NMS_WORLDSERVER_CLASS                  = null;
    public Class<?> NMS_PACKET_CLASS                       = null;
    public Class<?> NMS_PACKET_OUT_SPAWNENTITYLIVING_CLASS = null;
    public Class<?> NMS_PACKET_OUT_NAMEDENTITYSPAWN_CLASS  = null;
    public Class<?> NMS_PACKET_OUT_DESTROYENTITY_CLASS     = null;
    public Class<?> NMS_PACKET_OUT_WORLDPARTICLES_CLASS    = null;
    public Class<?> NMS_PACKET_OUT_ENTITYMETA_CLASS        = null;
    public Class<?> NMS_PACKET_OUT_ENTITYEQUIPMENT_CLASS   = null;
    public Class<?> NMS_PACKET_OUT_MOUNT_CLASS             = null;
    public Class<?> NMS_PACKET_OUT_ATTACHENTITY_CLASS      = null;
    public Class<?> NMS_PACKET_OUT_PLAYERINFO_CLASS        = null;
    public Class<?> NMS_PACKET_OUT_ENTITYTELEPORT          = null;
    public Class<?> NMS_PACKET_IN_USEENTITY_CLASS          = null;
    public Class<?> NMS_ENTITY_CLASS                       = null;
    public Class<?> NMS_ENTITY_LIVING_CLASS                = null;
    public Class<?> NMS_ENTITY_ARMORSTAND_CLASS            = null;
    public Class<?> NMS_ENTITY_HUMAN_CLASS                 = null;
    public Class<?> NMS_ENTITY_PLAYER_CLASS                = null;
    public Class<?> NMS_PLAYERINTERACTMANAGER_CLASS        = null;
    public Class<?> NMS_ENUM_PARTICLE_CLASS                = null;
    public Class<?> NMS_ENUM_PLAYERINFOACTION_CLASS        = null;
    public Class<?> NMS_ENUM_ITEMSLOT_CLASS                = null;
    public Class<?> NMS_DATAWATCHER_CLASS                  = null;
    public Class<?> NMS_DATAWATCHEROBJECT_CLASS            = null;
    public Class<?> NMS_DATAWATCHERREGISTRY_CLASS          = null;
    public Class<?> NMS_DATAWATCHERSERIALIZER_CLASS        = null;
    public Class<?> NMS_ITEMSTACK_CLASS                    = null;

    // Bukkit Classes
    public Class<?> BUKKIT_CRAFTSERVER_CLASS               = null;
    public Class<?> BUKKIT_CRAFTWORLD_CLASS                = null;
    public Class<?> BUKKIT_ENTITY_CRAFTPLAYER_CLASS        = null;
    public Class<?> BUKKIT_ENTITY_CRAFTENTITY_CLASS        = null;
    public Class<?> BUKKIT_INVENTORY_CRAFTITEMSTACK_CLASS  = null;

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
        this.NMS_MINECRAFTSERVER_CLASS = this.getNMSClass("MinecraftServer");
        this.NMS_WORLD_CLASS = this.getNMSClass("World");
        this.NMS_WORLDSERVER_CLASS = this.getNMSClass("WorldServer");
        this.NMS_PACKET_CLASS = this.getNMSClass("Packet");
        this.NMS_PACKET_OUT_SPAWNENTITYLIVING_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet24MobSpawn" : "PacketPlayOutSpawnEntityLiving");
        this.NMS_PACKET_OUT_NAMEDENTITYSPAWN_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet20NamedEntitySpawn" : "PacketPlayOutNamedEntitySpawn");
        this.NMS_PACKET_OUT_DESTROYENTITY_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet29DestroyEntity" : "PacketPlayOutEntityDestroy");
        this.NMS_PACKET_OUT_WORLDPARTICLES_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
        this.NMS_PACKET_OUT_ENTITYMETA_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet40EntityMetadata" : "PacketPlayOutEntityMetadata");
        this.NMS_PACKET_OUT_ENTITYEQUIPMENT_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet5EntityEquipment" : "PacketPlayOutEntityEquipment");
        this.NMS_PACKET_OUT_MOUNT_CLASS = this.getNMSClass("PacketPlayOutMount");
        this.NMS_PACKET_OUT_ATTACHENTITY_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet39AttachEntity" : "PacketPlayOutAttachEntity");
        this.NMS_PACKET_OUT_PLAYERINFO_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet201PlayerInfo" : "PacketPlayOutPlayerInfo");
        this.NMS_PACKET_OUT_ENTITYTELEPORT = this.getNMSClass(this.getIntVersion() < 7 ? "Packet34EntityTeleport" : "PacketPlayOutEntityTeleport");
        this.NMS_PACKET_IN_USEENTITY_CLASS = this.getNMSClass(this.getIntVersion() < 7 ? "Packet7UseEntity" : "PacketPlayInUseEntity");
        this.NMS_ENTITY_CLASS = this.getNMSClass("Entity");
        this.NMS_ENTITY_LIVING_CLASS = this.getNMSClass("EntityLiving");
        this.NMS_ENTITY_ARMORSTAND_CLASS = this.getNMSClass("EntityArmorStand");
        this.NMS_ENTITY_HUMAN_CLASS = this.getNMSClass("EntityHuman");
        this.NMS_ENTITY_PLAYER_CLASS = this.getNMSClass("EntityPlayer");
        this.NMS_PLAYERINTERACTMANAGER_CLASS = this.getNMSClass("PlayerInteractManager");
        this.NMS_ENUM_PARTICLE_CLASS = this.getNMSClass("EnumParticle");
        this.NMS_ENUM_PLAYERINFOACTION_CLASS = this.NMS_PACKET_OUT_PLAYERINFO_CLASS.getClasses()[1];
        this.NMS_ENUM_ITEMSLOT_CLASS = this.getNMSClass("EnumItemSlot");
        this.NMS_DATAWATCHER_CLASS = this.getNMSClass("DataWatcher");
        this.NMS_DATAWATCHEROBJECT_CLASS = this.getNMSClass("DataWatcherObject");
        this.NMS_DATAWATCHERREGISTRY_CLASS = this.getNMSClass("DataWatcherRegistry");
        this.NMS_DATAWATCHERSERIALIZER_CLASS = this.getNMSClass("DataWatcherSerializer");
        this.NMS_ITEMSTACK_CLASS = this.getNMSClass("ItemStack");

        // Bukkit
        this.BUKKIT_CRAFTSERVER_CLASS = this.getBukkitClass("CraftServer");
        this.BUKKIT_CRAFTWORLD_CLASS = this.getBukkitClass("CraftWorld");
        this.BUKKIT_ENTITY_CRAFTPLAYER_CLASS = this.getBukkitClass("entity.CraftPlayer");
        this.BUKKIT_ENTITY_CRAFTENTITY_CLASS = this.getBukkitClass("entity.CraftEntity");
        this.BUKKIT_INVENTORY_CRAFTITEMSTACK_CLASS = this.getBukkitClass("inventory.CraftItemStack");
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

    public Object getEntityHandle(Entity entity) {
        try {
            return ReflectionHelper.invokeDeclaredMethod("getHandle", this.BUKKIT_ENTITY_CRAFTENTITY_CLASS.cast(entity));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getWorldHandle(World world) {
        return this.getWorldHandle(world.toHandle());
    }

    public Object getWorldHandle(org.bukkit.World world) {
        try {
            return ReflectionHelper.invokeDeclaredMethod("getHandle", this.BUKKIT_CRAFTWORLD_CLASS.cast(world));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getPlayerConnection(Player player) {
        Object craftPlayer = this.BUKKIT_ENTITY_CRAFTPLAYER_CLASS.cast(player.toHandle());

        try {
            Object handle = ReflectionHelper.invokeDeclaredMethod("getHandle", craftPlayer);
            Object playerConnection = ReflectionHelper.getDeclaredField("playerConnection", handle);

            return playerConnection;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getItemStack(org.bukkit.inventory.ItemStack stack) {
        try {
            return ReflectionHelper.invokeStaticMethod("asNMSCopy", this.BUKKIT_INVENTORY_CRAFTITEMSTACK_CLASS, stack);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getItemStack(ItemStack stack) {
        try {
            return ReflectionHelper.invokeStaticMethod("asNMSCopy", this.BUKKIT_INVENTORY_CRAFTITEMSTACK_CLASS, stack.toHandle());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName() {
        return "nms";
    }
}