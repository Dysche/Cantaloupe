package org.cantaloupe.audio.network.packets;

import org.cantaloupe.audio.source.ISource;

public class S006PacketStop extends SXXXPacketSource {
    private S006PacketStop(ISource source) {
        super(source);
    }

    public static S006PacketStop of(ISource source) {
        return new S006PacketStop(source);
    }

    @Override
    public byte getID() {
        return 6;
    }
}