package org.cantaloupe.network.tcp.server.packets;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.TCPPacket;

public class S001PacketConnect extends TCPPacket {
    private S001PacketConnect(Session session) {
        super(session);
    }

    public static S001PacketConnect of(Session session) {
        return new S001PacketConnect(session);
    }

    @Override
    public byte getID() {
        return -127;
    }
}