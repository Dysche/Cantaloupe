package org.cantaloupe.audio.network.packets;

import java.util.UUID;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.web.WebClientPacket;
import org.json.simple.JSONObject;

public class C002PacketParams extends WebClientPacket {
    private UUID cid = null;
    private UUID uuid = null;
    
    private C002PacketParams(Session session) {
        super(session);
    }

    public static C002PacketParams of(Session session) {
        return new C002PacketParams(session);
    }

    @Override
    public void read(Object data) {
        super.read(data);

        this.cid = UUID.fromString((String) ((JSONObject) data).get("cid"));
        this.uuid = UUID.fromString((String) ((JSONObject) data).get("uuid"));
    }

    @Override
    public byte getID() {
        return 2;
    }

    public UUID getCID() {
        return this.cid;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
}