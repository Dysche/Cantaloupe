package org.cantaloupe.network.web;

import java.util.UUID;

import org.cantaloupe.network.session.Session;
import org.json.simple.JSONObject;

public abstract class WebClientPacket extends WebPacket {
    private Session session = null;

    protected WebClientPacket(Session session) {
        this.session = session;
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

    public Session getSession() {
        return this.session;
    }
}