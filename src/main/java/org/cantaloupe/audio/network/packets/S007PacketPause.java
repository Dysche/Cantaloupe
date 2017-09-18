package org.cantaloupe.audio.network.packets;

import org.cantaloupe.audio.source.ISource;

public class S007PacketPause extends SXXXPacketSource {
    private S007PacketPause(ISource source) {
        super(source);
    }

    public static S007PacketPause of(ISource source) {
        return new S007PacketPause(source);
    }

    @Override
    public byte getID() {
        return 7;
    }
}