package org.cantaloupe.network.web;

import org.cantaloupe.network.packet.IPacket;
import org.json.simple.JSONObject;

public abstract class WebPacket implements IPacket {
    @Override
    public void read(Object data) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject write() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pID", this.getID());
        
        return jsonObject;
    }
}