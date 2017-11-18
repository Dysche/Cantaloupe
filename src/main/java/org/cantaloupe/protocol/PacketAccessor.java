package org.cantaloupe.protocol;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.entity.FakeEntity;
import org.cantaloupe.entity.FakeEntityContainer;
import org.cantaloupe.events.PlayerAttackFakeEntityEvent;
import org.cantaloupe.events.PlayerInteractAtFakeEntityEvent;
import org.cantaloupe.events.PlayerInteractFakeEntityEvent;
import org.cantaloupe.events.PlayerSteerEvent;
import org.cantaloupe.events.SignInputEvent;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;
import org.joml.Vector3d;
import org.joml.Vector3i;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;

public class PacketAccessor {
    private static boolean interactLock   = false;
    private static boolean interactAtLock = false;

    public static void addFor(Player player) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
                if (packet.getClass() == service.NMS_PACKET_IN_USEENTITY_CLASS) {
                    if (handleUseEntityPacket(player, packet)) {
                        return;
                    }
                } else if (packet.getClass() == service.NMS_PACKET_IN_STEERVEHICLE_CLASS) {
                    handleSteerVehiclePacket(player, packet);
                } else if (packet.getClass() == service.NMS_PACKET_IN_UPDATESIGN_CLASS) {
                    if (handleUpdateSignPacket(player, packet)) {
                        return;
                    }
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

    private static boolean handleUseEntityPacket(Player player, Object packet) {
        try {
            int entityID = (int) ReflectionHelper.getDeclaredField("a", packet);
            Enum<?> action = (Enum<?>) ReflectionHelper.getDeclaredField("action", packet);

            FakeEntity entity = FakeEntityContainer.getEntity(entityID);
            if (entity != null) {
                if (!PacketVerifier.verifyEntityUsePacket(player, entity)) {
                    return true;
                }

                if (action.name().equals("INTERACT")) {
                    if (!interactLock) {
                        PlayerInteractFakeEntityEvent event = new PlayerInteractFakeEntityEvent(player, entity);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }

                    interactLock = !interactLock;
                } else if (action.name().equals("INTERACT_AT")) {
                    if (!interactAtLock) {
                        Object clickedPositionObject = ReflectionHelper.getDeclaredField("c", packet);
                        Vector3d clickedPosition = new Vector3d((double) ReflectionHelper.getField("x", clickedPositionObject), (double) ReflectionHelper.getField("y", clickedPositionObject), (double) ReflectionHelper.getField("z", clickedPositionObject));

                        PlayerInteractAtFakeEntityEvent event = new PlayerInteractAtFakeEntityEvent(player, entity, clickedPosition);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }

                    interactAtLock = !interactAtLock;
                } else if (action.name().equals("ATTACK")) {
                    PlayerAttackFakeEntityEvent event = new PlayerAttackFakeEntityEvent(player, entity);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }

                return true;
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void handleSteerVehiclePacket(Player player, Object packet) {
        try {
            boolean shift = (boolean) ReflectionHelper.getDeclaredField("d", packet);
            boolean space = (boolean) ReflectionHelper.getDeclaredField("c", packet);
            float forward = (float) ReflectionHelper.getDeclaredField("b", packet);
            float side = (float) ReflectionHelper.getDeclaredField("a", packet);

            Bukkit.getServer().getPluginManager().callEvent(new PlayerSteerEvent(player, shift, space, forward, side));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static boolean handleUpdateSignPacket(Player player, Object packet) {
        if (player.getCurrentSignInput() != null) {
            NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

            try {
                List<String> lines = new ArrayList<String>();

                Vector3i blockPosition = service.getBlockPositionFromHandle(ReflectionHelper.getDeclaredField("a", packet));
                Object[] components = (Object[]) ReflectionHelper.getDeclaredField("b", packet);

                for (Object object : components) {
                    if (object instanceof String) {
                        lines.add((String) object);
                    } else {
                        lines.add((String) ReflectionHelper.invokeDeclaredMethod("e", object));
                    }
                }

                if (player.getCurrentSignInput().getPosition().equals(blockPosition)) {
                    player.getCurrentSignInput().onInput(lines);

                    Bukkit.getServer().getPluginManager().callEvent(new SignInputEvent(player, lines));

                    return true;
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return false;
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