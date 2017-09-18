package org.cantaloupe.inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A class used to inject pieces of code into a method during runtime.
 * 
 * @author Dylan Scheltens
 *
 * @param <T>
 *            The class type
 */
public class Injector<T> {
    private Map<Scope, List<Consumer<T>>> consumers = null;

    protected Injector() {
        this.consumers = new HashMap<Scope, List<Consumer<T>>>();
    }

    /**
     * Creates and returns a new injector.
     * 
     * @param <T> The class type
     * @return The injector
     */
    public static <T> Injector<T> of() {
        return new Injector<T>();
    }

    /**
     * Injects a consumer into the injector.
     * 
     * @param scope
     *            The scope
     * @param consumer
     *            The consumer
     */
    public void inject(Scope scope, Consumer<T> consumer) {
        if (!this.consumers.containsKey(scope)) {
            this.consumers.put(scope, new ArrayList<Consumer<T>>());
        }

        this.consumers.get(scope).add(consumer);
    }

    /**
     * Injects a list of consumers into the injector.
     * 
     * @param other
     *            The other injector
     */
    public void injectAll(Injector<T> other) {
        other.consumers.forEach((scope, consumers) -> {
            if (!this.consumers.containsKey(scope)) {
                this.consumers.put(scope, new ArrayList<Consumer<T>>());
            }

            for (Consumer<T> consumer : consumers) {
                this.consumers.get(scope).add(consumer);
            }
        });
    }

    /**
     * Injects a list of consumers into the injector.
     * 
     * @param scope
     *            The scope
     * @param consumers
     *            The consumers
     */
    public void injectAll(Scope scope, List<Consumer<T>> consumers) {
        if (!this.consumers.containsKey(scope)) {
            this.consumers.put(scope, new ArrayList<Consumer<T>>());
        }

        for (Consumer<T> consumer : consumers) {
            this.consumers.get(scope).add(consumer);
        }
    }

    /**
     * Clears the injector.
     */
    public void clear() {
        for (List<Consumer<T>> consumers : this.consumers.values()) {
            consumers.clear();
        }

        this.consumers.clear();
    }

    /**
     * Accepts the consumers from the injector.
     * 
     * @param scope
     *            The scope
     * @param object
     *            The object
     */
    public void accept(Scope scope, T object) {
        Optional<List<Consumer<T>>> consumers = this.getConsumers(scope);

        if (consumers.isPresent()) {
            for (Consumer<T> consumer : consumers.get()) {
                consumer.accept(object);
            }
        }
    }

    /**
     * Gets a list of consumers from the injector.
     * 
     * @param scope
     *            The scope
     * @return An optional containing the list of consumers if they're present,
     *         an empty optional if not
     */
    public Optional<List<Consumer<T>>> getConsumers(Scope scope) {
        return Optional.ofNullable(this.consumers.get(scope));
    }
}