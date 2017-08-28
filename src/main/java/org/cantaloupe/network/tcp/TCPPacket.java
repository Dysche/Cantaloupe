package org.cantaloupe.network.tcp;

import java.util.UUID;

import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.session.Session;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class TCPPacket implements IPacket {
    private Session session;

    protected TCPPacket(Session session) {
        this.session = session;
    }

    @Override
    public void read(Object data) {
        ByteArrayDataInput input = (ByteArrayDataInput) data;

        this.session = Session.of(input.readUTF(), UUID.fromString(input.readUTF()));
    }

    @Override
    public ByteArrayDataOutput write() {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.write(this.getID());
        data.writeUTF(this.session.getHost());
        data.writeUTF(this.session.getID().toString());

        return data;
    }

    @Override
    public byte getID() {
        return 0;
    }

    public Session getSession() {
        return this.session;
    }
}