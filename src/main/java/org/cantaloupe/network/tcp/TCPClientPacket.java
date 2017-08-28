package org.cantaloupe.network.tcp;

import java.util.UUID;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.TCPPacket;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public abstract class TCPClientPacket extends TCPPacket {
    private Session session;

    protected TCPClientPacket(Session session) {
        this.session = session;
    }

    @Override
    public void read(Object data) {
        ByteArrayDataInput input = (ByteArrayDataInput) data;

        this.session = Session.of(input.readUTF(), UUID.fromString(input.readUTF()));
    }

    @Override
    public ByteArrayDataOutput write() {
        ByteArrayDataOutput data = super.write();
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