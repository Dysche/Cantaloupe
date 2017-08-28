package org.cantaloupe.network.tcp.server.packets;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.TCPPacket;

public class S000PacketSession extends TCPPacket {
    private S000PacketSession(Session session) {
        super(session);
    }

    public static S000PacketSession of(Session session) {
        return new S000PacketSession(session);
    }

    public static S000PacketSession of() {
        return new S000PacketSession(Session.of(""));
    }

    @Override
    public byte getID() {
        return -128;
    }
}