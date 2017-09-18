package org.cantaloupe.audio.network.packets;

import org.cantaloupe.audio.source.ISource;

public class S008PacketResume extends SXXXPacketSource {
    private S008PacketResume(ISource source) {
        super(source);
    }

    public static S008PacketResume of(ISource source) {
        return new S008PacketResume(source);
    }

    @Override
    public byte getID() {
        return 8;
    }
}