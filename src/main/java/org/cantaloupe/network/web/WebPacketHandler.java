package org.cantaloupe.network.web;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.packet.IPacketListener;
import org.cantaloupe.network.session.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WebPacketHandler {
    private final List<IPacketListener>                         listeners;
    private final DataContainer<Byte, Class<? extends IPacket>> packetClasses;

    public WebPacketHandler() {
        this.listeners = new ArrayList<IPacketListener>();
        this.packetClasses = DataContainer.of();
    }

    public void registerPacketClass(byte packetID, Class<? extends IPacket> packetClass) {
        this.packetClasses.put(packetID, packetClass);
    }

    public void registerListener(IPacketListener listener) {
        this.listeners.add(listener);
    }

    public void unregisterListener(IPacketListener listener) {
        this.listeners.remove(listener);
    }

    public void sendPacket(IConnection connection, IPacket packet) {
        connection.sendPacket(packet);
    }

    public void handlePacket(IConnection connection, String message) {
        try {
            JSONObject object = (JSONObject) new JSONParser().parse(message);
            byte packetID = Byte.parseByte(object.get("ID").toString());

            if (this.packetClasses.containsKey(packetID)) {
                WebPacket packet = null;

                try {
                    Constructor<?> constructor = this.packetClasses.get(packetID).getDeclaredConstructor(Session.class);
                    constructor.setAccessible(true);

                    packet = (WebPacket) constructor.newInstance(new Object[] {
                            null
                    });

                    packet.read(object);

                    for (IPacketListener listener : this.listeners) {
                        listener.onPacketRecieved(connection, packet);
                    }
                } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}