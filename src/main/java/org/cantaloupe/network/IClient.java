package org.cantaloupe.network;

import org.cantaloupe.inject.Injector;

public interface IClient {
    public void connect();

    public void disconnect();

    public IConnection getConnection();
    
    public Injector<? extends IConnection> getInjector();
}