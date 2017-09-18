package org.cantaloupe.network.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.cantaloupe.data.DataContainer;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.network.IServer;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.packet.PacketHandler;
import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.client.packets.C000PacketConnect;
import org.cantaloupe.network.tcp.client.packets.C001PacketDisconnect;
import org.cantaloupe.network.tcp.server.packets.S000PacketSession;

public class TCPServer implements IServer {
    private final int                                         port;
    private final DataContainer<Session, TCPServerConnection> connections;
    private final PacketHandler                               packetHandler;
    private final Injector<TCPServerConnection>               injector;

    private ServerSocket                                      socket  = null;
    private boolean                                           running = false;

    private TCPServer(int port) {
        this.port = port;
        this.connections = DataContainer.of();

        this.packetHandler = PacketHandler.of();
        this.packetHandler.registerListener(new TCPServerPacketListener(this));
        this.packetHandler.registerClientPacketClass((byte) 0, C000PacketConnect.class);
        this.packetHandler.registerClientPacketClass((byte) 1, C001PacketDisconnect.class);

        this.injector = Injector.of();
    }

    public static TCPServer of(int port) {
        return new TCPServer(port);
    }

    public void inject(Scope scope, Consumer<TCPServerConnection> consumer) {
        this.injector.inject(scope, consumer);
    }

    public void injectAll(Scope scope, List<Consumer<TCPServerConnection>> consumers) {
        this.injector.injectAll(scope, consumers);
    }

    @Override
    public void start() {
        try {
            this.socket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.running = true;
        this.startThread();
    }

    @Override
    public void stop() {
        this.running = false;

        if (!this.socket.isClosed()) {
            this.connections.forEach((session, connection) -> {
                connection.close();
            });

            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.connections.clear();
        }
    }

    private void startThread() {
        final TCPServer server = this;

        new Thread() {
            @Override
            public void run() {
                while (running) {
                    Socket clientSocket = null;

                    if (getSocket() != null && !getSocket().isClosed()) {
                        try {
                            clientSocket = socket.accept();
                        } catch (IOException e) {
                            if (!e.getMessage().equals("socket closed")) {
                                e.printStackTrace();
                            }

                            break;
                        }

                        TCPServerConnection connection = new TCPServerConnection(server, clientSocket);
                        connection.setSession(Session.of(clientSocket.getRemoteSocketAddress().toString()));
                        connection.open();
                        connection.sendPacket(S000PacketSession.of(connection.getSession()));

                        connections.put(connection.getSession(), connection);
                    }
                }
            }
        }.start();
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
            this.connections.get(session).close();
            this.connections.remove(session);
        }
    }

    public boolean isConnected(Session session) {
        return this.connections.containsKey(session);
    }

    public ServerSocket getSocket() {
        return this.socket;
    }

    public TCPServerConnection getConnection(Session session) {
        return this.connections.get(session);
    }

    public Collection<TCPServerConnection> getConnections() {
        return this.connections.valueSet();
    }

    public PacketHandler getPacketHandler() {
        return this.packetHandler;
    }
    
    @Override
    public Injector<TCPServerConnection> getInjector() {
        return this.injector;
    }

    public static class Scopes {
        public static final Scope CONNECTED    = Scope.of("tcp_server", "connected");
        public static final Scope DISCONNECTED = Scope.of("tcp_server", "disconnected");
    }
}