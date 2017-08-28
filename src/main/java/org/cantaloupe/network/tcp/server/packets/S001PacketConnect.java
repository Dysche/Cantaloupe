package org.cantaloupe.network.tcp.server.packets;

import java.util.UUID;

import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.TCPServerPacket;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class S001PacketConnect extends TCPServerPacket {
    private Session session = null;

    private S001PacketConnect(Session session) {
        this.session = session;
    }

    public static S001PacketConnect of(Session session) {
        return new S001PacketConnect(session);
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
        return -127;
    }

    public Session getSession() {
        return this.session;
    }
}