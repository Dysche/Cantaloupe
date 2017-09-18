package org.cantaloupe.network.packet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.TCPPacket;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

/**
 * A class used to handle packets.
 * 
 * @author Dylan Scheltens
 *
 */
public class PacketHandler {
    private final List<IPacketListener>                         listeners;
    private final DataContainer<Byte, Class<? extends IPacket>> serverPacketClasses;
    private final DataContainer<Byte, Class<? extends IPacket>> clientPacketClasses;

    private PacketHandler() {
        this.listeners = new ArrayList<IPacketListener>();
        this.serverPacketClasses = DataContainer.of();
        this.clientPacketClasses = DataContainer.of();
    }

    /**
     * Creates and returns a new packet handler.
     * 
     * @return The packet handler
     */
    public static PacketHandler of() {
        return new PacketHandler();
    }

    /**
     * Registers a server packet class to the packet handler.
     * 
     * @param packetID
     *            The ID of the packet
     * @param packetClass
     *            The type of the packet
     */
    public void registerServerPacketClass(byte packetID, Class<? extends IPacket> packetClass) {
        this.serverPacketClasses.put(packetID, packetClass);
    }

    /**
     * Registers a client packet class to the packet handler.
     * 
     * @param packetID
     *            The ID of the packet
     * @param packetClass
     *            The type of the packet
     */
    public void registerClientPacketClass(byte packetID, Class<? extends IPacket> packetClass) {
        this.clientPacketClasses.put(packetID, packetClass);
    }

    /**
     * Registers a packet listener to the packet handler.
     * 
     * @param listener
     *            The listener
     */
    public void registerListener(IPacketListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Unregisters a packet listener from the packet handler.
     * 
     * @param listener
     *            The listener
     */
    public void unregisterListener(IPacketListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Sends a packet to the connection.
     * 
     * @param connection
     *            The connection
     * @param packet
     *            The packet
     */
    public void sendPacket(IConnection connection, IPacket packet) {
        connection.sendPacket(packet);
    }

    /**
     * Handles a packet coming in from a connection.
     * 
     * @param connection
     *            The connection
     * @param bytes
     *            The packet data
     * @param fromServer
     *            Whether or not the packet came from a server
     */
    public void handlePacket(IConnection connection, byte[] bytes, boolean fromServer) {
        DataContainer<Byte, Class<? extends IPacket>> packetClasses = fromServer ? this.serverPacketClasses : this.clientPacketClasses;
        ByteArrayDataInput data = ByteStreams.newDataInput(bytes);
        byte packetID = data.readByte();

        if (packetClasses.containsKey(packetID)) {
            TCPPacket packet = null;

            try {
                Constructor<?> constructor = packetClasses.get(packetID).getDeclaredConstructor(Session.class);
                constructor.setAccessible(true);

                packet = (TCPPacket) constructor.newInstance(new Object[] {
                        null
                });

                packet.read(ByteStreams.newDataInput(bytes, 1));

                for (IPacketListener listener : this.listeners) {
                    listener.onPacketRecieved(connection, packet);
                }
            } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}