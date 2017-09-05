package org.cantaloupe.audio;

import java.util.UUID;

import org.cantaloupe.network.web.server.WebServerConnection;
import org.cantaloupe.player.Player;
import org.cantaloupe.player.PlayerWrapper;

public class AudioWrapper extends PlayerWrapper {
    private WebServerConnection connection = null;
    private UUID                cid        = null;

    public AudioWrapper(Player player) {
        super(player);
    }

    @Override
    public void onUnload() {
        if (this.isConnected()) {
            this.connection.closeExt();
        }
    }

    public String connect(String host) {
        this.cid = UUID.randomUUID();

        return host + "?cid=" + this.cid.toString() + "&uuid=" + this.getPlayer().getUUID().toString();
    }

    public void disconnect() {
        this.cid = null;
        this.connection = null;
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    public void setConnection(WebServerConnection connection) {
        this.connection = connection;
    }

    public WebServerConnection getConnection() {
        return this.connection;
    }

    public UUID getCID() {
        return this.cid;
    }
}