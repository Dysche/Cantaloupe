package org.cantaloupe.network.packet;

public interface IPacket {
    public void read(Object data);

    public Object write();
    
    public byte getID();
}