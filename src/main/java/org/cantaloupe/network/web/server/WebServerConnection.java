package org.cantaloupe.network.web.server;

import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.web.WebPacket;
import org.java_websocket.WebSocket;

public class WebServerConnection implements IConnection {
    private final WebServer server;
    private final WebSocket socket;

    private Session         session = null;

    protected WebServerConnection(WebServer server, WebSocket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void open() {}

    @Override
    public void close() {}

    @Override
    public void sendPacket(IPacket packet) {
        this.sendMessage(((WebPacket) packet).write().toJSONString());
    }

    private void sendMessage(String message) {
        synchronized (this.socket) {
            this.socket.send(message);
        }
    }

    protected void setSession(Session session) {
        this.session = session;
    }

    public WebServer getServer() {
        return this.server;
    }

    public WebSocket getSocket() {
        return this.socket;
    }

    public Session getSession() {
        return this.session;
    }
}