package org.cantaloupe.audio.network.packets;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.web.WebClientPacket;
import org.json.simple.JSONObject;

public class C003PacketSync extends WebClientPacket {
    private String sourceID = null;

    private C003PacketSync(Session session) {
        super(session);
    }

    public static C003PacketSync of(Session session) {
        return new C003PacketSync(session);
    }

    @Override
    public void read(Object data) {
        this.sourceID = (String) ((JSONObject) data).get("srcID");
    }

    @Override
    public byte getID() {
        return 3;
    }

    public String getSourceID() {
        return this.sourceID;
    }
}