package org.cantaloupe.audio.network.packets;

import org.cantaloupe.audio.source.ISource;
import org.json.simple.JSONObject;

public class S010PacketPan extends SXXXPacketSource {
    private int pan = 0;

    private S010PacketPan(ISource source, int pan) {
        super(source);

        this.pan = pan;
    }

    public static S010PacketPan of(ISource source, int pan) {
        return new S010PacketPan(source, pan);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject write() {
        JSONObject jsonObject = super.write();
        jsonObject.put("pan", this.pan);

        return jsonObject;
    }

    @Override
    public byte getID() {
        return 10;
    }
    
    public int getPan() {
        return this.pan;
    }
}