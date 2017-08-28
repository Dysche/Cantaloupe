package org.cantaloupe.network.web.client.packets;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.web.WebClientPacket;

public class C000PacketConnect extends WebClientPacket {
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