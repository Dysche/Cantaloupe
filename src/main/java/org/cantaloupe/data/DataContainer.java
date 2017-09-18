package org.cantaloupe.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * A class used to store data in a map-like structure.
 * 
 * @author Dylan Scheltens
 *
 * @param <K>
 *            The key type
 * @param <V>
 *            The value type
 */
@SuppressWarnings("unchecked")
public class DataContainer<K, V> implements IFlushable {
    public static final long    serialVersionUID = 3047031121107082509L;
    private final HashMap<K, V> handle;
    private final boolean       sorted;

    protected DataContainer(boolean sorted) {
        this.handle = sorted ? new LinkedHashMap<K, V>() : new HashMap<K, V>();
        this.sorted = sorted;
    }

    /**
     * Creates a new data container.
     * 
     * @param <K>
     *            The key type
     * @param <V>
     *            The value type
     * @param sorted
     *            Whether or not the container is sorted.
     * @return The container
     */
    public static <K, V> DataContainer<K, V> of(boolean sorted) {
        return new DataContainer<K, V>(sorted);
    }

    /**
     * Creates a new data container.
     * 
     * @param <K>
     *            The key type
     * @param <V>
     *            The value type
     * @return The container
     */
    public static <K, V> DataContainer<K, V> of() {
        return new DataContainer<K, V>(false);
    }

    /**
     * Stores an entry in the container.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The container
     */
    public DataContainer<K, V> put(K key, V value) {
        this.handle.put(key, value);

        return this;
    }

    /**
     * Stores the entries of a map in the container.
     * 
     * @param map
     *            The map
     * @return The container
     */
    public DataContainer<K, V> put(Map<K, V> map) {
        map.forEach((key, value) -> {
            this.handle.put(key, value);
        });

        return this;
    }

    /**
     * Stores the entries of another container in the container.
     * 
     * @param other
     *            The other container
     * @return The container
     */
    public DataContainer<K, V> put(DataContainer<K, V> other) {
        other.forEach((key, value) -> {
            this.handle.put(key, value);
        });

        return this;
    }

    /**
     * Removes an entry from the container.
     * 
     * @param key
     *            The key of the entry
     * @return The container
     */
    public DataContainer<K, V> remove(K key) {
        this.handle.remove(key);

        return this;
    }

    /**
     * Checks if the container has an entry.
     * 
     * @param key
     *            The key of the entry
     * @return True if it does, false if not
     */
    public boolean containsKey(K key) {
        return this.handle.containsKey(key);
    }

    /**
     * Checks if the container has a value.
     * 
     * @param value
     *            The value
     * @return True if it does, false if not
     */
    public boolean containsValue(V value) {
        return this.handle.containsValue(value);
    }

    /**
     * Checks if an entry's value is null
     * 
     * @param key
     *            The key of the entry
     * @return True if it is, false if not
     */
    public boolean isNull(K key) {
        return this.get(key) == null;
    }

    /**
     * Clears the container.
     * 
     * @return The container.
     */
    public DataContainer<K, V> clear() {
        this.handle.clear();

        return this;
    }

    /**
     * @see java.util.Map#forEach(BiConsumer)
     * 
     * @param consumer
     *            The consumer
     */
    public void forEach(BiConsumer<? super K, ? super V> consumer) {
        this.handle.forEach(consumer);
    }

    /**
     * Returns the size of the container.
     * 
     * @return The size
     */
    public int size() {
        return this.handle.size();
    }

    /**
     * Creates a branch of the container by entry type.
     * 
     * @param <T>
     *            The value type
     * @param clazz
     *            The type
     * @return The sub-container
     */
    public <T extends V> DataContainer<K, T> branch(Class<T> clazz) {
        DataContainer<K, T> toReturn = DataContainer.of();

        this.handle.forEach((key, value) -> {
            if (value.getClass() == clazz) {
                toReturn.put(key, (T) value);
            }
        });

        return toReturn;
    }

    /**
     * Gets a random key from the container.
     * 
     * @return The key
     */
    public K getRandomKey() {
        Object[] keys = this.handle.keySet().toArray();

        return (K) keys[new Random().nextInt(keys.length)];
    }

    /**
     * Gets a random value from the container.
     * 
     * @return The value
     */
    public V getRandomValue() {
        Object[] values = this.handle.values().toArray();

        return (V) values[new Random().nextInt(values.length)];
    }

    /**
     * Gets a random value from the container by entry type.
     * 
     * @param <T>
     *            The type to cast to
     * @param clazz
     *            The type
     * @return The value
     */
    public <T extends V> T getRandomValue(Class<T> clazz) {
        Object[] values = this.branch(clazz).valueSet().toArray();

        return (T) values[new Random().nextInt(values.length)];
    }

    /**
     * Gets and casts a value from the container.
     * 
     * @param key
     *            The entry's key
     * @return The value
     */
    public V get(K key) {
        return this.handle.containsKey(key) ? this.handle.get(key) : null;
    }

    /**
     * Gets and casts a value from the container.
     * 
     * @param key
     *            The key of the entry
     * @param clazz
     *            The type
     * @return The value
     */
    public V get(K key, Class<? extends V> clazz) {
        return clazz.cast(this.get(key));
    }

    /**
     * Gets a template value from the container.
     * 
     * @param <T>
     *            The type to cast to
     * @param key
     *            The key of the entry
     * @return The value
     */
    public <T> T getGeneric(K key) {
        return (T) this.get(key);
    }

    /**
     * Gets and casts a template value from the container.
     * 
     * @param <T>
     *            The type to cast to
     * @param key
     *            The key of the entry
     * @param clazz
     *            The type
     * @return The value
     */
    public <T> T getGeneric(K key, Class<? extends T> clazz) {
        return clazz.cast(this.get(key));
    }

    /**
     * Returns the handle of the container.
     * 
     * @return The handle
     */
    public HashMap<K, V> toHandle() {
        return this.handle;
    }

    /**
     * Returns a set of keys from the container.
     * 
     * @return The set of keys
     */
    public Set<K> keySet() {
        return this.handle.keySet();
    }

    /**
     * Returns a collection of values from the container.
     * 
     * @return The set of keys
     */
    public Collection<V> valueSet() {
        return this.handle.values();
    }

    /**
     * Clones the container.
     * 
     * @param sorted
     *            Whether or not the entries are sorted
     * @return The clone
     */
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