package org.cantaloupe.network;

import org.cantaloupe.inject.Injector;
import org.cantaloupe.network.packet.IPacket;

public interface IConnection {
    public void open();

    public void close();
    
    public void sendPacket(IPacket packet);
}