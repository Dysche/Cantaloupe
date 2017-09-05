package org.cantaloupe.audio;

import java.util.List;
import java.util.function.Consumer;

import org.cantaloupe.audio.network.ASPacketListener;
import org.cantaloupe.audio.network.packets.C002PacketParams;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.network.web.server.WebServer;

public class AudioServer {
    private final WebServer              server;
    private final Injector<AudioWrapper> injector;

    private AudioServer(int port) {
        this.server = WebServer.of(port);
        this.server.getPacketHandler().registerListener(new ASPacketListener(this));
        this.server.getPacketHandler().registerClientPacketClass((byte) 2, C002PacketParams.class);
        
        this.injector = Injector.of();
    }

    public static AudioServer of(int port) {
        return new AudioServer(port);
    }
    
    public void inject(Scope scope, Consumer<AudioWrapper> consumer) {
        this.injector.inject(scope, consumer);
    }

    public void injectAll(Scope scope, List<Consumer<AudioWrapper>> consumers) {
        this.injector.injectAll(scope, consumers);
    }

    public void start() {
        this.server.start();
    }

    public void stop() {
        this.server.stop();
    }

    public WebServer getServer() {
        return this.server;
    }

    public Injector<AudioWrapper> getInjector() {
        return this.injector;
    }

    public static class Scopes {
        public static final Scope SESSION_BEGIN = Scope.of("audioserver", "session_begin");
        public static final Scope SESSION_END   = Scope.of("audioserver", "session_end");
    }
}