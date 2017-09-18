package org.cantaloupe.audio.network.packets;

import org.cantaloupe.audio.source.ISource;
import org.json.simple.JSONObject;

public class S011PacketVolumePan extends SXXXPacketSource {
    private int volume = 0;
    private int pan    = 0;

    private S011PacketVolumePan(ISource source, int volume, int pan) {
        super(source);

        this.volume = volume;
        this.pan = pan;
    }

    public static S011PacketVolumePan of(ISource source, int volume, int pan) {
        return new S011PacketVolumePan(source, volume, pan);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject write() {
        JSONObject jsonObject = super.write();
        jsonObject.put("volume", this.volume);
        jsonObject.put("pan", this.pan);

        return jsonObject;
    }

    @Override
    public byte getID() {
        return 11;
    }
    
    public int getVolume() {
        return this.volume;
    }
    
    public int getPan() {
        return this.pan;
    }
}