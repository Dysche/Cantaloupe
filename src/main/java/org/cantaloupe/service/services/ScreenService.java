package org.cantaloupe.service.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.IService;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.ReflectionHelper;

import net.md_5.bungee.chat.ComponentSerializer;

public class ScreenService implements IService {
    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    // Title
    public void title(Text titleText, int duration, Collection<Player> players) {
        this.title(titleText, null, -1, duration, -1, players.toArray(new Player[0]));
    }

    public void title(Text titleText, int duration, Player... players) {
        this.title(titleText, null, -1, duration, -1, players);
    }

    public void title(Text titleText, int fadeIn, int duration, int fadeOut, Collection<Player> players) {
        this.title(titleText, null, fadeIn, duration, fadeOut, players.toArray(new Player[0]));
    }

    public void title(Text titleText, int fadeIn, int duration, int fadeOut, Player... players) {
        this.title(titleText, null, fadeIn, duration, fadeOut, players);
    }

    public void title(Text titleText, Text subText, int duration, Collection<Player> players) {
        this.title(titleText, subText, -1, duration, -1, players.toArray(new Player[0]));
    }

    public void title(Text titleText, Text subText, int duration, Player... players) {
        this.title(titleText, subText, -1, duration, -1, players);
    }

    public void title(Text titleText, Text subText, int fadeIn, int duration, int fadeOut, Collection<Player> players) {
        this.title(titleText, subText, fadeIn, duration, fadeOut, players.toArray(new Player[0]));
    }

    public void title(Text titleText, Text subText, int fadeIn, int duration, int fadeOut, Player... players) {
        this.sendTitlePacket(titleText, subText, fadeIn, duration, fadeOut, players);
    }

    public void subTitle(Text text, int duration, Collection<Player> players) {
        this.subTitle(text, -1, duration, -1, players.toArray(new Player[0]));
    }

    public void subTitle(Text text, int duration, Player... players) {
        this.subTitle(text, -1, duration, -1, players);
    }

    public void subTitle(Text text, int fadeIn, int duration, int fadeOut, Collection<Player> players) {
        this.subTitle(text, fadeIn, duration, fadeOut, players.toArray(new Player[0]));
    }

    public void subTitle(Text text, int fadeIn, int duration, int fadeOut, Player... players) {
        this.sendSubTitlePacket(text, fadeIn, duration, fadeOut, players);
    }

    public void titleTiming(int duration, Collection<Player> players) {
        this.titleTiming(-1, duration, -1, players.toArray(new Player[0]));
    }

    public void titleTiming(int duration, Player... players) {
        this.titleTiming(-1, duration, -1, players);
    }

    public void titleTiming(int fadeIn, int duration, int fadeOut, Collection<Player> players) {
        this.titleTiming(fadeIn, duration, fadeOut, players.toArray(new Player[0]));
    }

    public void titleTiming(int fadeIn, int duration, int fadeOut, Player... players) {
        this.sendTitleTimingPacket(fadeIn, duration, fadeOut, players);
    }

    public void clearTitle(Collection<Player> players) {
        this.clearTitle(players.toArray(new Player[0]));
    }

    public void clearTitle(Player... players) {
        this.sendTitleClearPacket(players);
    }

    // Actionbar
    public void actionBar(Text text, Player... players) {
        this.sendActionBarPacket(text, players);
    }

    public void clearActionBar(Player... players) {
        this.actionBar(null, players);
    }

    private void sendTitlePacket(Text titleText, Text subText, int fadeIn, int duration, int fadeOut, Player... players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packetTitle = titleText != null
                    ? nmsService.NMS_PACKET_OUT_TITLE.getConstructor(nmsService.NMS_ENUM_TITLEACTION_CLASS, nmsService.NMS_ICHATBASECOMPONENT_CLASS, int.class, int.class, int.class).newInstance(ReflectionHelper.getStaticField("TITLE", nmsService.NMS_ENUM_TITLEACTION_CLASS),
                            ReflectionHelper.invokeStaticMethod("a", nmsService.NMS_CHATSERIALIZER_CLASS, ComponentSerializer.toString(titleText.getComponent())), fadeIn, duration, fadeOut)
                    : nmsService.NMS_PACKET_OUT_TITLE.getConstructor(nmsService.NMS_ENUM_TITLEACTION_CLASS, nmsService.NMS_ICHATBASECOMPONENT_CLASS, int.class, int.class, int.class).newInstance(ReflectionHelper.getStaticField("CLEAR", nmsService.NMS_ENUM_TITLEACTION_CLASS), null, -1, -1, -1);

            Object packetSubtitle = subText != null ? nmsService.NMS_PACKET_OUT_TITLE.getConstructor(nmsService.NMS_ENUM_TITLEACTION_CLASS, nmsService.NMS_ICHATBASECOMPONENT_CLASS, int.class, int.class, int.class).newInstance(ReflectionHelper.getStaticField("SUBTITLE", nmsService.NMS_ENUM_TITLEACTION_CLASS),
                    ReflectionHelper.invokeStaticMethod("a", nmsService.NMS_CHATSERIALIZER_CLASS, ComponentSerializer.toString(subText.getComponent())), fadeIn, duration, fadeOut) : null;

            for (Player player : players) {
                packetService.sendPacket(player, packetTitle);

                if (packetSubtitle != null) {
                    packetService.sendPacket(player, packetSubtitle);
                }
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void sendSubTitlePacket(Text text, int fadeIn, int duration, int fadeOut, Player... players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = nmsService.NMS_PACKET_OUT_TITLE.getConstructor(nmsService.NMS_ENUM_TITLEACTION_CLASS, nmsService.NMS_ICHATBASECOMPONENT_CLASS, int.class, int.class, int.class).newInstance(ReflectionHelper.getStaticField("SUBTITLE", nmsService.NMS_ENUM_TITLEACTION_CLASS),
                    ReflectionHelper.invokeStaticMethod("a", nmsService.NMS_CHATSERIALIZER_CLASS, ComponentSerializer.toString(text.getComponent())), fadeIn, duration, fadeOut);

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void sendTitleTimingPacket(int fadeIn, int duration, int fadeOut, Player... players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = nmsService.NMS_PACKET_OUT_TITLE.getConstructor(nmsService.NMS_ENUM_TITLEACTION_CLASS, nmsService.NMS_ICHATBASECOMPONENT_CLASS, int.class, int.class, int.class).newInstance(ReflectionHelper.getStaticField("CLEAR", nmsService.NMS_ENUM_TITLEACTION_CLASS),
                    ReflectionHelper.invokeStaticMethod("a", nmsService.NMS_CHATSERIALIZER_CLASS, null, fadeIn, duration, fadeOut));

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void sendTitleClearPacket(Player... players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object packet = nmsService.NMS_PACKET_OUT_TITLE.getConstructor(nmsService.NMS_ENUM_TITLEACTION_CLASS, nmsService.NMS_ICHATBASECOMPONENT_CLASS, int.class, int.class, int.class).newInstance(ReflectionHelper.getStaticField("CLEAR", nmsService.NMS_ENUM_TITLEACTION_CLASS),
                    ReflectionHelper.invokeStaticMethod("a", nmsService.NMS_CHATSERIALIZER_CLASS, null, -1, -1, -1));

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void sendActionBarPacket(Text text, Player... players) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);
        PacketService packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        try {
            Object component = ReflectionHelper.invokeStaticMethod("a", nmsService.NMS_CHATSERIALIZER_CLASS, text != null ? "{\"text\":\"" + text.toLegacy() + "\"}" : "{\"text\":\"\"}");
            Object packet = nmsService.NMS_PACKET_OUT_CHAT.getConstructor(nmsService.NMS_ICHATBASECOMPONENT_CLASS, byte.class).newInstance(component, (byte) 2);

            for (Player player : players) {
                packetService.sendPacket(player, packet);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "screen";
    }
}