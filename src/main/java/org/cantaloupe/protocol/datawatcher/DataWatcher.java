package org.cantaloupe.protocol.datawatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;

import com.google.common.collect.Maps;

public class DataWatcher {
    private final Object                            entity;
    private final Map<Integer, DataWatcher.Item<?>> items;

    private DataWatcher(Object entity) {
        this.entity = entity;
        this.items = Maps.newHashMap();
    }

    /**
     * Creates and returns a new datawatcher.
     * 
     * @param entity
     *            The entity
     * @return The datawatcher
     */
    public static DataWatcher of(Object entity) {
        return new DataWatcher(entity);
    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    public <T> void register(DataWatcherObject<T> object, Object value) {
        this.items.put(Integer.valueOf(object.getIndex()), new Item(object, value));
    }

    /**
     * Returns the NMS version of this datawatcher.
     * 
     * @return The NMS version
     */
    public Object toNMS() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            Object handle = service.NMS_DATAWATCHER_CLASS.getConstructor(service.NMS_ENTITY_CLASS).newInstance(new Object[] {
                    this.entity
            });

            for (Item<?> item : this.items.values()) {
                ReflectionHelper.invokeMethod("register", handle, new Class<?>[] {
                        service.NMS_DATAWATCHEROBJECT_CLASS, Object.class
                }, item.getObject().toNMS(), item.value);
            }

            return handle;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class Item<T> {
        private final DataWatcherObject<T> object;
        private T                          value = null;
        private boolean                    dirty = true;

        public Item(DataWatcherObject<T> object, T value) {
            this.object = object;
            this.value = value;
        }

        public DataWatcherObject<T> getObject() {
            return this.object;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public T getValue() {
            return this.value;
        }

        public boolean isDirty() {
            return this.dirty;
        }

        public void setDirty(boolean flag) {
            this.dirty = flag;
        }
    }
}