package org.cantaloupe.audio.network.packets;

import org.cantaloupe.network.web.WebServerPacket;

public class S003PacketCIDVerified extends WebServerPacket {
    private S003PacketCIDVerified() {
        
    }

    public static S003PacketCIDVerified of() {
        return new S003PacketCIDVerified();
    }

    @Override
    public byte getID() {
        return 3;
    }
}