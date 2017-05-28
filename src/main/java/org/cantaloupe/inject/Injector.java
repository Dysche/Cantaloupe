package org.cantaloupe.inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class Injector<T> {
    private Map<Scope, List<Consumer<T>>> consumers = null;

    public Injector() {
        this.consumers = new HashMap<Scope, List<Consumer<T>>>();
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

    public Optional<List<Consumer<T>>> getConsumers(Scope scope) {
        return Optional.ofNullable(this.consumers.get(scope));
    }
}