package org.cantaloupe.network.tcp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.session.Session;

import com.google.common.io.ByteArrayDataOutput;

public class TCPServerConnection implements IConnection {
    private final TCPServer server;
    private final Socket    socket;

    private Session         session      = null;
    private InputStream     inputStream  = null;
    private OutputStream    outputStream = null;

    protected TCPServerConnection(TCPServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void open() {
        final TCPServerConnection connection = this;

        try {
            this.inputStream = this.socket.getInputStream();
            this.outputStream = this.socket.getOutputStream();

            new Thread() {
                @Override
                public void run() {
                    byte[] bytes = new byte[1024];

                    while (!socket.isClosed()) {
                        try {
                            if (inputStream.available() > 0) {
                                inputStream.read(bytes);
                                server.getPacketHandler().handlePacket(connection, bytes);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();

                            break;
                        }
                    }

                    if (!socket.isClosed()) {
                        server.closeConnection(session);
                    }
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            this.inputStream.close();
            this.outputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public TCPServer getServer() {
        return this.server;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public Session getSession() {
        return this.session;
    }
}