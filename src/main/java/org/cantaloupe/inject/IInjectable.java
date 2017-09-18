package org.cantaloupe.inject;

import java.util.List;
import java.util.function.Consumer;

/**
 * An interface implemented by classes containing an injector.
 * 
 * @author Dylan Scheltens
 *
 * @param <T>
 *            The accepted class type
 */
public interface IInjectable<T> {
    /**
     * Injects a consumer into the injector.
     * 
     * @param scope
     *            The scope
     * @param consumer
     *            The consumer
     */
    default void inject(Scope scope, Consumer<T> consumer) {
        this.getInjector().inject(scope, consumer);
    }

    /**
     * Injects a list of consumers into the injector.
     * 
     * @param scope
     *            The scope
     * @param consumers
     *            The consumers
     */
    default void injectAll(Scope scope, List<Consumer<T>> consumers) {
        this.getInjector().injectAll(scope, consumers);
    }

    /**
     * Gets the injector.
     * 
     * @return The injector
     */
    Injector<T> getInjector();
}
