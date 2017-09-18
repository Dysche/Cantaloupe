package org.cantaloupe.audio;

import java.util.List;
import java.util.function.Consumer;

import org.cantaloupe.audio.network.ASPacketListener;
import org.cantaloupe.audio.network.packets.C002PacketParams;
import org.cantaloupe.audio.network.packets.C003PacketSync;
import org.cantaloupe.audio.sound.SoundManager;
import org.cantaloupe.audio.source.SourceManager;
import org.cantaloupe.inject.IInjectable;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inject.Scope;
import org.cantaloupe.network.web.server.WebServer;

/**
 * A class used to create a connection between cantaloupe and the audio client.
 * 
 * @author Dylan Scheltens
 *
 */
public class AudioServer implements IInjectable<AudioWrapper> {
    private final WebServer              server;
    private final Injector<AudioWrapper> injector;
    private final SourceManager          sourceManager;
    private final SoundManager           soundManager;

    private AudioServer(int port) {
        this.server = WebServer.of(port);
        this.server.getPacketHandler().registerListener(new ASPacketListener(this));
        this.server.getPacketHandler().registerClientPacketClass((byte) 2, C002PacketParams.class);
        this.server.getPacketHandler().registerClientPacketClass((byte) 3, C003PacketSync.class);

        this.injector = Injector.of();
        this.sourceManager = SourceManager.of();
        this.soundManager = SoundManager.of();

        this.inject(AudioServer.Scopes.SESSION_END, wrapper -> {
            wrapper.clearSourceSettings();
        });
    }

    /**
     * Creates and returns a new audio server.
     * 
     * @param port
     *            The port to listen to
     * @return The audio server
     */
    public static AudioServer of(int port) {
        return new AudioServer(port);
    }

    @Override
    public void inject(Scope scope, Consumer<AudioWrapper> consumer) {
        this.injector.inject(scope, consumer);
    }

    @Override
    public void injectAll(Scope scope, List<Consumer<AudioWrapper>> consumers) {
        this.injector.injectAll(scope, consumers);
    }

    /**
     * Starts the audio server.
     */
    public void start() {
        this.server.start();
    }

    /**
     * Stops the audio server.
     */
    public void stop() {
        this.server.stop();
    }

    /**
     * Checks if the server is running/
     * 
     * @return True if it's running, false if not
     */
    public boolean isRunning() {
        return this.server.isRunning();
    }

    /**
     * Returns the web server of this audio server.
     * 
     * @return The web server
     */
    public WebServer getServer() {
        return this.server;
    }

    @Override
    public Injector<AudioWrapper> getInjector() {
        return this.injector;
    }

    /**
     * Returns the source manager of this audio server.
     * 
     * @return The source manager
     */
    public SourceManager getSourceManager() {
        return this.sourceManager;
    }

    /**
     * Returns the sound manager of this audio server.
     * 
     * @return The sound manager
     */
    public SoundManager getSoundManager() {
        return this.soundManager;
    }

    public static class Scopes {
        public static final Scope SESSION_BEGIN = Scope.of("audioserver", "session_begin");
        public static final Scope SESSION_END   = Scope.of("audioserver", "session_end");
    }
}