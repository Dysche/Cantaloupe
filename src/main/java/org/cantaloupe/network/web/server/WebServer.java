package org.cantaloupe.network.web.server;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.network.IServer;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.web.WebPacketHandler;
import org.cantaloupe.network.web.client.packets.C000PacketConnect;
import org.cantaloupe.network.web.client.packets.C001PacketDisconnect;
import org.cantaloupe.network.web.server.packets.S000PacketSession;
import org.cantaloupe.network.web.server.packets.S002PacketDisconnect;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebServer extends WebSocketServer implements IServer {
    private final DataContainer<Session, WebServerConnection> connections;
    private final WebPacketHandler                            packetHandler;
    private final Injector<WebServerConnection>               injector;

    private WebServer(InetSocketAddress address) {
        super(address);

        this.connections = DataContainer.of();

        this.packetHandler = new WebPacketHandler();
        this.packetHandler.registerListener(new WebServerPacketListener(this));
        this.packetHandler.registerClientPacketClass((byte) 0, C000PacketConnect.class);
        this.packetHandler.registerClientPacketClass((byte) 1, C001PacketDisconnect.class);

        this.injector = Injector.of();
    }

    private WebServer(int port) {
        this(new InetSocketAddress(port));
    }

    public static WebServer of(int port) {
        return new WebServer(port);
    }

    public static WebServer of(InetSocketAddress address) {
        return new WebServer(address);
    }

    public void inject(Scope scope, Consumer<WebServerConnection> consumer) {
        this.injector.inject(scope, consumer);
    }

    public void injectAll(Scope scope, List<Consumer<WebServerConnection>> consumers) {
        this.injector.injectAll(scope, consumers);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        for (WebSocket socket : this.connections()) {
            socket.close(CloseFrame.NORMAL);
        }

        try {
            Field field = WebSocketServer.class.getDeclaredField("server");
            field.setAccessible(true);

            ServerSocketChannel channel = (ServerSocketChannel) field.get(this);
            channel.close();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }

        try {
            super.stop(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(Session session, IPacket packet) {
        if (this.isConnected(session)) {
            this.connections.get(session).sendPacket(packet);
        }
    }

    @Override
    public void broadcast(IPacket packet) {
        this.connections.forEach((session, connection) -> {
            connection.sendPacket(packet);
        });
    }

    public void closeConnection(Session session) {
        if (this.isConnected(session)) {
            WebServerConnection connection = this.connections.get(session);
            connection.close();

            this.connections.remove(session);

            this.injector.accept(Scopes.DISCONNECTED, connection);
            connection.getInjector().accept(Scopes.DISCONNECTED, connection);
        }
    }

    public boolean isConnected(Session session) {
        return this.connections.containsKey(session);
    }

    @Override
    public void onOpen(WebSocket socket, ClientHandshake handshake) {
        WebServerConnection connection = new WebServerConnection(this, socket);
        connection.setSession(Session.of(socket.getRemoteSocketAddress().getAddress().getHostAddress()));
        connection.sendPacket(S000PacketSession.of(connection.getSession()));

        this.connections.put(connection.getSession(), connection);

        this.injector.accept(Scopes.CONNECTED, connection);
        connection.getInjector().accept(Scopes.CONNECTED, connection);
    }

    @Override
    public void onClose(WebSocket socket, int code, String reason, boolean remote) {
        WebServerConnection connection = this.getConnectionBySocket(socket);

        if (connection != null) {
            this.connections.remove(connection.getSession());
            this.broadcast(S002PacketDisconnect.of(connection.getSession()));

            this.injector.accept(Scopes.DISCONNECTED, connection);
            connection.getInjector().accept(Scopes.DISCONNECTED, connection);
        }
    }

    @Override
    public void onMessage(WebSocket socket, String message) {
        this.packetHandler.handlePacket(this.getConnectionBySocket(socket), message, false);
    }

    @Override
    public void onError(WebSocket socket, Exception exception) {
        WebServerConnection connection = this.getConnectionBySocket(socket);

        if (connection != null) {
            this.connections.remove(connection.getSession());
            this.broadcast(S002PacketDisconnect.of(connection.getSession()));
        }

        exception.printStackTrace();
    }

    public WebServerConnection getConnection(Session session) {
        return this.connections.get(session);
    }

    public WebServerConnection getConnectionBySocket(WebSocket socket) {
        for (WebServerConnection connection : this.connections.valueSet()) {
            if (connection.getSocket() == socket) {
                return connection;
            }
        }

        return null;
    }

    public Collection<WebServerConnection> getConnections() {
        return this.connections.valueSet();
    }

    public WebPacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    @Override
    public Injector<WebServerConnection> getInjector() {
        return this.injector;
    }

    public static class Scopes {
        public static final Scope CONNECTED    = Scope.of("tcp_server", "connected");
        public static final Scope DISCONNECTED = Scope.of("tcp_server", "disconnected");
    }
}