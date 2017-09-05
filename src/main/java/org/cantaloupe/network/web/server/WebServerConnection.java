package org.cantaloupe.network.web.server;

import java.util.List;
import java.util.function.Consumer;

import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.web.WebPacket;
import org.cantaloupe.network.web.server.packets.S002PacketDisconnect;
import org.java_websocket.WebSocket;

public class WebServerConnection implements IConnection {
    private final WebServer                     server;
    private final WebSocket                     socket;
    private final Injector<WebServerConnection> injector;

    private Session                             session = null;

    protected WebServerConnection(WebServer server, WebSocket socket) {
        this.server = server;
        this.socket = socket;
        this.injector = Injector.of();
    }

    public void inject(Scope scope, Consumer<WebServerConnection> consumer) {
        this.injector.inject(scope, consumer);
    }

    public void injectAll(Scope scope, List<Consumer<WebServerConnection>> consumers) {
        this.injector.injectAll(scope, consumers);
    }
    
    @Override
    public void open() {}

    @Override
    public void close() {
        this.server.broadcast(S002PacketDisconnect.of(this.session));
    }

    @Override
    public void closeExt() {
        this.server.closeConnection(this.session);
    }

    @Override
    public void sendPacket(IPacket packet) {
        this.sendMessage(((WebPacket) packet).write().toJSONString());
    }

    private void sendMessage(String message) {
        this.socket.send(message);
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
    
    public Injector<WebServerConnection> getInjector() {
        return this.injector;
    }
}