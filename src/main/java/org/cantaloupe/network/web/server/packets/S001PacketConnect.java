package org.cantaloupe.network.web.server.packets;

import java.util.UUID;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.web.WebServerPacket;
import org.json.simple.JSONObject;

public class S001PacketConnect extends WebServerPacket {
    private Session session = null;

    private S001PacketConnect(Session session) {
        this.session = session;
    }

    public static S001PacketConnect of(Session session) {
        return new S001PacketConnect(session);
    }

    @Override
    public void read(Object data) {
        JSONObject object = (JSONObject) data;

        this.session = Session.of((String) object.get("sHost"), UUID.fromString((String) object.get("sID")));
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject write() {
        JSONObject jsonObject = super.write();
        jsonObject.put("sHost", this.session.getHost());
        jsonObject.put("sID", this.session.getID().toString());

        return jsonObject;
    }

    @Override
    public byte getID() {
        return 1;
    }

    public Session getSession() {
        return this.session;
    }
}