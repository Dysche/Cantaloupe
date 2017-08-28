package org.cantaloupe.network.web.client.packets;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.web.WebClientPacket;

public class C001PacketDisconnect extends WebClientPacket {
    private C001PacketDisconnect(Session session) {
        super(session);
    }

    public static C001PacketDisconnect of(Session session) {
        return new C001PacketDisconnect(session);
    }

    @Override
    public byte getID() {
        return 1;
    }
}