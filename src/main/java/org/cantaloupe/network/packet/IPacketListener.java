package org.cantaloupe.network.packet;

import org.cantaloupe.network.IConnection;

public interface IPacketListener {
    public void onPacketRecieved(IConnection connection, IPacket packet);
}