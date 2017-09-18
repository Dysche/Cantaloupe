package org.cantaloupe.audio.network.packets;

import org.cantaloupe.audio.source.ISource;
import org.json.simple.JSONObject;

public class S009PacketVolume extends SXXXPacketSource {
    private int volume = 0;

    private S009PacketVolume(ISource source, int volume) {
        super(source);

        this.volume = volume;
    }

    public static S009PacketVolume of(ISource source, int volume) {
        return new S009PacketVolume(source, volume);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject write() {
        JSONObject jsonObject = super.write();
        jsonObject.put("volume", this.volume);

        return jsonObject;
    }

    @Override
    public byte getID() {
        return 9;
    }
    
    public int getVolume() {
        return this.volume;
    }
}