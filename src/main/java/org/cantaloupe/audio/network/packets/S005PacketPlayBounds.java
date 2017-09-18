package org.cantaloupe.audio.network.packets;

import org.cantaloupe.audio.source.ISource;
import org.json.simple.JSONObject;

public class S005PacketPlayBounds extends SXXXPacketSource {
    private int volume = 0;
    private int pan    = 0;
    private int begin  = -1;
    private int end    = -1;

    private S005PacketPlayBounds(ISource source, int volume, int pan, int begin, int end) {
        super(source);

        this.volume = volume;
        this.pan = pan;
        this.begin = begin;
        this.end = end;
    }

    public static S005PacketPlayBounds of(ISource source, int volume, int pan, int begin, int end) {
        return new S005PacketPlayBounds(source, volume, pan, begin, end);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject write() {
        JSONObject jsonObject = super.write();
        jsonObject.put("path", this.source.getSound().getPath());
        jsonObject.put("volume", this.volume);
        jsonObject.put("pan", this.pan);
        jsonObject.put("begin", this.begin);
        jsonObject.put("end", this.end);

        return jsonObject;
    }

    @Override
    public byte getID() {
        return 5;
    }

    public int getVolume() {
        return this.volume;
    }

    public int getPan() {
        return this.pan;
    }

    public int getBegin() {
        return this.begin;
    }

    public int getEnd() {
        return this.end;
    }
}