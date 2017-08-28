package org.cantaloupe.network.tcp.client;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;

import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.network.IClient;

public class TCPClient implements IClient {
    private final String        host;
    private final int           port;

    private Socket              socket     = null;
    private TCPClientConnection connection = null;

    private TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static TCPClient of(String host, int port) {
        return new TCPClient(host, port);
    }

    public void inject(Scope scope, Consumer<TCPClientConnection> consumer) {
        this.connection.inject(scope, consumer);
    }

    public void injectAll(Scope scope, List<Consumer<TCPClientConnection>> consumers) {
        this.connection.injectAll(scope, consumers);
    }

    @Override
    public void connect() {
        try {
            this.socket = new Socket(this.host, this.port);

            this.connection = new TCPClientConnection(this);
            this.connection.open();
            this.connection.getInjector().accept(Scopes.CONNECTED, this.connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        this.connection.close();

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.connection.getInjector().accept(Scopes.DISCONNECTED, this.connection);
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }

    protected Socket getSocket() {
        return this.socket;
    }

    @Override
    public TCPClientConnection getConnection() {
        return this.connection;
    }

    @Override
    public Injector<TCPClientConnection> getInjector() {
        return this.connection.getInjector();
    }
    
    public static class Scopes {
        public static final Scope CONNECTED    = Scope.of("tcp_client", "connected");
        public static final Scope DISCONNECTED = Scope.of("tcp_client", "disconnected");
    }
}