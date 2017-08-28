package org.cantaloupe.network.tcp;

import org.cantaloupe.network.packet.IPacket;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public abstract class TCPPacket implements IPacket {
    @Override
    public abstract void read(Object data);

    @Override
    public ByteArrayDataOutput write() {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.write(this.getID());

        return data;
    }

    @Override
    public byte getID() {
        return 0;
    }
}