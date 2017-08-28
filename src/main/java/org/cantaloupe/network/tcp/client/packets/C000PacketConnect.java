package org.cantaloupe.network.tcp.client.packets;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.TCPPacket;

public class C000PacketConnect extends TCPPacket {
    private C000PacketConnect(Session session) {
        super(session);
    }

    public static C000PacketConnect of(Session session) {
        return new C000PacketConnect(session);
    }

    @Override
    public byte getID() {
        return 0;
    }
}