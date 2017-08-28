package org.cantaloupe.network.web.server;

import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.packet.IPacketListener;
import org.cantaloupe.network.web.client.packets.C000PacketConnect;
import org.cantaloupe.network.web.client.packets.C001PacketDisconnect;
import org.cantaloupe.network.web.server.WebServer.Scopes;
import org.cantaloupe.network.web.server.packets.S001PacketConnect;
import org.cantaloupe.network.web.server.packets.S002PacketDisconnect;

public class WebServerPacketListener implements IPacketListener {
    private final WebServer server;

    protected WebServerPacketListener(WebServer server) {
        this.server = server;
    }

    @Override
    public void onPacketRecieved(IConnection connection, IPacket packet) {
        WebServerConnection clientConnection = (WebServerConnection) connection;

        if (packet instanceof C000PacketConnect) {
            this.server.getConnections().forEach(c -> {
                c.sendPacket(S001PacketConnect.of(((C000PacketConnect) packet).getSession()));
            });

            this.server.getInjector().accept(Scopes.CONNECTED, clientConnection);
        } else if (packet instanceof C001PacketDisconnect) {
            this.server.getConnections().forEach(c -> {
                c.sendPacket(S002PacketDisconnect.of(((C001PacketDisconnect) packet).getSession()));
            });

            this.server.getInjector().accept(Scopes.DISCONNECTED, clientConnection);
        }
    }
}