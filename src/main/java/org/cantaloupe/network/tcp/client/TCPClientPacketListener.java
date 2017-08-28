package org.cantaloupe.network.tcp.client;

import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.packet.IPacketListener;
import org.cantaloupe.network.tcp.client.packets.C000PacketConnect;
import org.cantaloupe.network.tcp.server.packets.S000PacketSession;
import org.cantaloupe.network.tcp.server.packets.S001PacketConnect;
import org.cantaloupe.network.tcp.server.packets.S002PacketDisconnect;

public class TCPClientPacketListener implements IPacketListener {
    protected TCPClientPacketListener() {

    }

    @Override
    public void onPacketRecieved(IConnection connection, IPacket packet) {
        TCPClientConnection clientConnection = (TCPClientConnection) connection;
        
        if (packet instanceof S000PacketSession) {
            clientConnection.setSession(((S000PacketSession) packet).getSession());
            clientConnection.sendPacket(C000PacketConnect.of(clientConnection.getSession()));
            
            System.out.println("test");
        } else if (packet instanceof S001PacketConnect) {
            System.out.println("C: " + ((S001PacketConnect) packet).getSession());
        } else if (packet instanceof S002PacketDisconnect) {
            System.out.println("D: " + ((S002PacketDisconnect) packet).getSession());
        }
    }
}