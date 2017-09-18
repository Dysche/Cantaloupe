package org.cantaloupe.audio.network.packets;

import org.cantaloupe.audio.source.ISource;
import org.cantaloupe.audio.source.ITimableSource;
import org.json.simple.JSONObject;

public class S012PacketSync extends SXXXPacketSource {
    private S012PacketSync(ISource source) {
        super(source);
    }

    public static S012PacketSync of(ISource source) {
        return new S012PacketSync(source);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject write() {
        JSONObject jsonObject = super.write();
        jsonObject.put("elapsedTime", ((ITimableSource) this.getSource()).getElapsedTime());

        return jsonObject;
    }

    @Override
    public byte getID() {
        return 12;
    }

    public int getElapsedTime() {
        return ((ITimableSource) this.getSource()).getElapsedTime();
    }
}