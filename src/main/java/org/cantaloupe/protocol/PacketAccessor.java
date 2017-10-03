package org.cantaloupe.protocol;

import org.bukkit.Bukkit;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.entity.FakeEntity;
import org.cantaloupe.entity.FakeEntityContainer;
import org.cantaloupe.events.PlayerAttackFakeEntityEvent;
import org.cantaloupe.events.PlayerInteractAtFakeEntityEvent;
import org.cantaloupe.events.PlayerInteractFakeEntityEvent;
import org.cantaloupe.events.PlayerSteerEvent;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;
import org.joml.Vector3d;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;

public class PacketAccessor {
    public static void addFor(Player player) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
                if (packet.getClass() == service.NMS_PACKET_IN_USEENTITY_CLASS) {
                    handleUseEntityPacket(player, packet);
                } else if (packet.getClass() == service.NMS_PACKET_IN_STEERVEHICLE_CLASS) {
                    handleSteerVehiclePacket(player, packet);
                }

                super.channelRead(context, packet);
            }

            @Override
            public void write(ChannelHandlerContext context, Object packet, ChannelPromise channelPromise) throws Exception {
                super.write(context, packet, channelPromise);
            }
        };

        ChannelPipeline pipeline = getChannel(player).pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

    public static void removeFor(Player player) {
        Channel channel = getChannel(player);

        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());

            return null;
        });
    }

    private static void handleUseEntityPacket(Player player, Object packet) {
        try {
            int entityID = (int) ReflectionHelper.getDeclaredField("a", packet);
            Enum<?> action = (Enum<?>) ReflectionHelper.getDeclaredField("action", packet);

            FakeEntity entity = FakeEntityContainer.getEntity(entityID);
            if (entity != null) {
                if (!PacketVerifier.verifyEntityUsePacket(player, entity)) {
                    return;
                }

                if (action.name().equals("INTERACT")) {
                    PlayerInteractFakeEntityEvent event = new PlayerInteractFakeEntityEvent(player, entity);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                } else if (action.name().equals("INTERACT_AT")) {
                    Object clickedPositionObject = ReflectionHelper.getDeclaredField("c", packet);
                    Vector3d clickedPosition = new Vector3d((double) ReflectionHelper.getField("x", clickedPositionObject), (double) ReflectionHelper.getField("y", clickedPositionObject), (double) ReflectionHelper.getField("z", clickedPositionObject));

                    PlayerInteractAtFakeEntityEvent event = new PlayerInteractAtFakeEntityEvent(player, entity, clickedPosition);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                } else if (action.name().equals("ATTACK")) {
                    PlayerAttackFakeEntityEvent event = new PlayerAttackFakeEntityEvent(player, entity);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void handleSteerVehiclePacket(Player player, Object packet) {
        try {
            boolean shift = (boolean) ReflectionHelper.getDeclaredField("d", packet);
            boolean space = (boolean) ReflectionHelper.getDeclaredField("c", packet);
            float forward = (float) ReflectionHelper.getDeclaredField("b", packet);
            float side = (float) ReflectionHelper.getDeclaredField("a", packet);

            PlayerSteerEvent event = new PlayerSteerEvent(player, shift, space, forward, side);
            Bukkit.getServer().getPluginManager().callEvent(event);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Channel getChannel(Player player) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object connection = nmsService.getPlayerConnection(player);
            Object networkManager = ReflectionHelper.getField("networkManager", connection);

            return (Channel) ReflectionHelper.getField("channel", networkManager);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}