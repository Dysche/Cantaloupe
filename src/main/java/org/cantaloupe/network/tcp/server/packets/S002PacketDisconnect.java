package org.cantaloupe.network.tcp.server.packets;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.TCPPacket;

public class S002PacketDisconnect extends TCPPacket {
    private S002PacketDisconnect(Session session) {
        super(session);
    }

    public static S002PacketDisconnect of(Session session) {
        return new S002PacketDisconnect(session);
    }

    @Override
    public byte getID() {
        return -126;
    }
}