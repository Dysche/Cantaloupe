package org.cantaloupe.service.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.Service;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;

public class LightService implements Service {
    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Deprecated
    public void addLight(ImmutableLocation location, int level, Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object world = nmsService.getWorldHandle(location.getWorld());
            Object blockPosition = nmsService.getBlockPosition(location.getBlockPosition());

            ReflectionHelper.invokeMethod("a", world, new Class<?>[] {
                nmsService.NMS_ENUM_SKYBLOCK_CLASS, nmsService.NMS_BLOCKPOSITION_CLASS, int.class
            }, ReflectionHelper.getStaticField("BLOCK", nmsService.NMS_ENUM_SKYBLOCK_CLASS), blockPosition, level);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        
        this.updateChunk(location, players);
    }

    @Deprecated
    public void removeLight(ImmutableLocation location, Collection<Player> players) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object world = service.getWorldHandle(location.getWorld());
            Object blockPosition = service.getBlockPosition(location.getBlockPosition());

            ReflectionHelper.invokeMethod("c", world, new Class<?>[] {
                    service.NMS_ENUM_SKYBLOCK_CLASS, service.NMS_BLOCKPOSITION_CLASS, int.class
            }, service.NMS_ENUM_SKYBLOCK_CLASS.getField("BLOCK"), blockPosition);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.updateChunk(location, players);
    }

    private void updateChunk(ImmutableLocation location, Collection<Player> players) {
        this.sendChunkUpdate(location.getWorld(), location.getChunkPosition().x, location.getChunkPosition().z, location, players);
    }

    private void sendChunkUpdate(World world, int chunkX, int chunkZ, ImmutableLocation location, Collection<Player> players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = null;
            Object nmsWorld = nmsService.getWorldHandle(world);
            Object nmsChunk = ReflectionHelper.invokeMethod("getChunkAt", nmsWorld, nmsService.NMS_WORLD_CLASS, new Class<?>[] {
                    int.class, int.class
            }, chunkX, chunkZ);

            if (nmsService.getIntVersion() > 9) {
                nmsService.NMS_PACKET_OUT_MAPCHUNK.getConstructor(nmsService.NMS_CHUNK_CLASS, int.class).newInstance(nmsChunk, 65535);
            } else {
                nmsService.NMS_PACKET_OUT_MAPCHUNK.getConstructor(nmsService.NMS_CHUNK_CLASS, boolean.class, int.class).newInstance(nmsChunk, false, 65535);
            }

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "light";
    }
}