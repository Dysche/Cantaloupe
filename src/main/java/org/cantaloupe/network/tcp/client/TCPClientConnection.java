package org.cantaloupe.network.tcp.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.packet.PacketHandler;
import org.cantaloupe.network.session.Session;
import org.cantaloupe.network.tcp.client.packets.C001PacketDisconnect;
import org.cantaloupe.network.tcp.server.packets.S000PacketSession;
import org.cantaloupe.network.tcp.server.packets.S001PacketConnect;
import org.cantaloupe.network.tcp.server.packets.S002PacketDisconnect;

import com.google.common.io.ByteArrayDataOutput;

public class TCPClientConnection implements IConnection {
    private final TCPClient                     client;
    private final PacketHandler                 packetHandler;
    private final Injector<TCPClientConnection> injector;
    
    private Session                             session;
    private InputStream                         inputStream  = null;
    private OutputStream                        outputStream = null;

    protected TCPClientConnection(TCPClient client) {
        this.client = client;

        this.packetHandler = new PacketHandler();
        this.packetHandler.registerListener(new TCPClientPacketListener());
        this.packetHandler.registerServerPacketClass((byte) 0, S000PacketSession.class);
        this.packetHandler.registerServerPacketClass((byte) 1, S001PacketConnect.class);
        this.packetHandler.registerServerPacketClass((byte) 2, S002PacketDisconnect.class);

        this.injector = Injector.of();
    }

    public void inject(Scope scope, Consumer<TCPClientConnection> consumer) {
        this.injector.inject(scope, consumer);
    }

    public void injectAll(Scope scope, List<Consumer<TCPClientConnection>> consumers) {
        this.injector.injectAll(scope, consumers);
    }

    @Override
    public void open() {
        final TCPClientConnection connection = this;

        try {
            this.inputStream = this.client.getSocket().getInputStream();
            this.outputStream = this.client.getSocket().getOutputStream();

            new Thread() {
                @Override
                public void run() {
                    byte[] bytes = new byte[1024];

                    while (!client.getSocket().isClosed()) {
                        try {
                            if (inputStream.available() > 0) {
                                inputStream.read(bytes);
                                packetHandler.handlePacket(connection, bytes, true);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();

                            break;
                        }
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        this.sendPacket(C001PacketDisconnect.of(this.session));

        try {
            this.inputStream.close();
            this.outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeExt() {
        this.client.disconnect();
    }
    
    @Override
    public void sendPacket(IPacket packet) {
        try {
            this.outputStream.write(((ByteArrayDataOutput) packet.write()).toByteArray());
            this.outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setSession(Session session) {
        this.session = session;
    }

    public boolean isConnected() {
        return this.client.isConnected();
    }

    public TCPClient getClient() {
        return this.client;
    }
    
    public PacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public Session getSession() {
        return this.session;
    }
    
    public Injector<TCPClientConnection> getInjector() {
        return this.injector;
    }
}