package org.cantaloupe.inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class Injector<T> {
    private Map<Scope, List<Consumer<T>>> consumers = null;

    protected Injector() {
        this.consumers = new HashMap<Scope, List<Consumer<T>>>();
    }

    public static <T> Injector<T> of() {
        return new Injector<T>();
    }

    public void inject(Scope scope, Consumer<T> consumer) {
        if (!this.consumers.containsKey(scope)) {
            this.consumers.put(scope, new ArrayList<Consumer<T>>());
        }

        this.consumers.get(scope).add(consumer);
    }

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

    public void injectAll(Scope scope, List<Consumer<T>> consumers) {
        if (!this.consumers.containsKey(scope)) {
            this.consumers.put(scope, new ArrayList<Consumer<T>>());
        }

        for (Consumer<T> consumer : consumers) {
            this.consumers.get(scope).add(consumer);
        }
    }

    public void clear() {
        for (List<Consumer<T>> consumers : this.consumers.values()) {
            consumers.clear();
        }

        this.consumers.clear();
    }

    public void accept(Scope scope, T object) {
        Optional<List<Consumer<T>>> consumers = this.getConsumers(scope);

        if (consumers.isPresent()) {
            for (Consumer<T> consumer : consumers.get()) {
                consumer.accept(object);
            }
        }
    }

    public Optional<List<Consumer<T>>> getConsumers(Scope scope) {
        return Optional.ofNullable(this.consumers.get(scope));
    }
}