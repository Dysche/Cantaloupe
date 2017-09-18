package org.cantaloupe.audio.network;

import java.util.Optional;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.audio.AudioServer;
import org.cantaloupe.audio.AudioServer.Scopes;
import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.audio.network.packets.C002PacketParams;
import org.cantaloupe.audio.network.packets.C003PacketSync;
import org.cantaloupe.audio.network.packets.S003PacketCIDVerified;
import org.cantaloupe.audio.network.packets.S012PacketSync;
import org.cantaloupe.audio.source.ISource;
import org.cantaloupe.network.IConnection;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.packet.IPacketListener;
import org.cantaloupe.network.web.server.WebServer;
import org.cantaloupe.network.web.server.WebServerConnection;
import org.cantaloupe.player.Player;

public class ASPacketListener implements IPacketListener {
    private final AudioServer server;

    public ASPacketListener(AudioServer server) {
        this.server = server;
    }

    @Override
    public void onPacketRecieved(IConnection connection, IPacket packet) {
        WebServerConnection clientConnection = (WebServerConnection) connection;

        if (packet instanceof C002PacketParams) {
            Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayer(((C002PacketParams) packet).getUUID());

            if (playerOpt.isPresent()) {
                Player player = playerOpt.get();
                Optional<AudioWrapper> wrapperOpt = player.getWrapper(AudioWrapper.class);

                if (wrapperOpt.isPresent()) {
                    AudioWrapper wrapper = wrapperOpt.get();

                    if (wrapper.getCID() != null) {
                        if (wrapper.getCID().equals(((C002PacketParams) packet).getCID())) {
                            wrapper.setConnection(clientConnection);

                            clientConnection.inject(WebServer.Scopes.DISCONNECTED, c -> {
                                this.server.getInjector().accept(Scopes.SESSION_END, wrapper);

                                wrapper.disconnect();
                            });

                            this.server.getInjector().accept(Scopes.SESSION_BEGIN, wrapper);

                            clientConnection.sendPacket(S003PacketCIDVerified.of());
                        }
                    }
                }
            } else {
                connection.closeExt();
            }
        } else if (packet instanceof C003PacketSync) {
            Optional<ISource> sourceOpt = Cantaloupe.getAudioServer().get().getSourceManager().getSource(((C003PacketSync) packet).getSourceID());

            if (sourceOpt.isPresent()) {
                clientConnection.sendPacket(S012PacketSync.of(sourceOpt.get()));
            }
        }
    }
}