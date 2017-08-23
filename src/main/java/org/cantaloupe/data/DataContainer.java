package org.cantaloupe.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class DataContainer<K, V> implements Flushable {
    public static final long    serialVersionUID = 3047031121107082509L;
    private final HashMap<K, V> handle;
    private final boolean       sorted;

    protected DataContainer(boolean sorted) {
        this.handle = sorted ? new LinkedHashMap<K, V>() : new HashMap<K, V>();
        this.sorted = sorted;
    }

    public static <K, V> DataContainer<K, V> of(boolean sorted) {
        return new DataContainer<K, V>(sorted);
    }

    public static <K, V> DataContainer<K, V> of() {
        return new DataContainer<K, V>(false);
    }

    public DataContainer<K, V> put(K key, V value) {
        this.handle.put(key, value);

        return this;
    }

    public DataContainer<K, V> put(HashMap<K, V> other) {
        other.forEach((key, value) -> {
            this.handle.put(key, value);
        });

        return this;
    }

    public DataContainer<K, V> put(DataContainer<K, V> other) {
        other.forEach((key, value) -> {
            this.handle.put(key, value);
        });

        return this;
    }

    public DataContainer<K, V> remove(K key) {
        this.handle.remove(key);

        return this;
    }

    public boolean containsKey(K key) {
        return this.handle.containsKey(key);
    }

    public boolean containsValue(V value) {
        return this.handle.containsValue(value);
    }

    public boolean isNull(K key) {
        return this.get(key) == null;
    }

    public DataContainer<K, V> clear() {
        this.handle.clear();

        return this;
    }

    public void forEach(BiConsumer<? super K, ? super V> consumer) {
        this.handle.forEach(consumer);
    }

    public int size() {
        return this.handle.size();
    }

    public <T extends V> DataContainer<K, T> branch(Class<T> clazz) {
        DataContainer<K, T> toReturn = DataContainer.of();

        this.handle.forEach((key, value) -> {
            if (value.getClass() == clazz) {
                toReturn.put(key, (T) value);
            }
        });

        return toReturn;
    }

    public K getRandomKey() {
        Object[] keys = this.handle.keySet().toArray();

        return (K) keys[new Random().nextInt(keys.length)];
    }

    public V getRandomValue() {
        Object[] values = this.handle.values().toArray();

        return (V) values[new Random().nextInt(values.length)];
    }

    public <T extends V> T getRandomValue(Class<T> clazz) {
        Object[] values = this.branch(clazz).valueSet().toArray();

        return (T) values[new Random().nextInt(values.length)];
    }

    public V get(K key) {
        return this.handle.containsKey(key) ? this.handle.get(key) : null;
    }

    public V get(K key, Class<? extends V> clazz) {
        return clazz.cast(this.get(key));
    }

    public <T> T getGeneric(K key) {
        return (T) this.get(key);
    }

    public <T> T getGeneric(K key, Class<? extends T> clazz) {
        return clazz.cast(this.get(key));
    }

    public HashMap<K, V> toHandle() {
        return this.handle;
    }

    public Set<K> keySet() {
        return this.handle.keySet();
    }

    public Collection<V> valueSet() {
        return this.handle.values();
    }

    public DataContainer<K, V> clone(boolean sorted) {
        DataContainer<K, V> clone = DataContainer.of(sorted);
        clone.put(this);

        return clone;
    }
    
    @Override
    public DataContainer<K, V> clone() {
        return this.clone(this.sorted);
    }

    @Override
    public void flush() {
        for (K key : this.handle.keySet()) {
            System.out.println("[" + key + ", " + this.get(key) + "]");
        }
    }

    @Override
    public boolean equals(Object o) {
        return this.handle.equals(o);
    }
}