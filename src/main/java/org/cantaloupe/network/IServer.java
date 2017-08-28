package org.cantaloupe.network;

import org.cantaloupe.inject.Injector;
import org.cantaloupe.network.packet.IPacket;
import org.cantaloupe.network.session.Session;

public interface IServer {
    public void start();

    public void stop();

    public void sendPacket(Session session, IPacket packet);

    public void broadcast(IPacket packet);
    
    public Injector<? extends IConnection> getInjector();
}