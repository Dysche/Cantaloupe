package org.cantaloupe.protocol.datawatcher;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;

public class DataWatcherObject<T> {
    private final int                      index;
    private final DataWatcherSerializer<T> serializer;

    private DataWatcherObject(int index, DataWatcherSerializer<T> serializer) {
        this.index = index;
        this.serializer = serializer;
    }

    public static <T> DataWatcherObject<T> of(int index, DataWatcherSerializer<T> serializer) {
        return new DataWatcherObject<T>(index, serializer);
    }

    protected Object toNMS() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return service.NMS_DATAWATCHEROBJECT_CLASS.getConstructor(int.class, service.NMS_DATAWATCHERSERIALIZER_CLASS).newInstance(this.index, ReflectionHelper.getStaticField(this.serializer.getFieldName() + "", service.NMS_DATAWATCHERREGISTRY_CLASS));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getIndex() {
        return this.index;
    }

    public DataWatcherSerializer<T> getSerializer() {
        return this.serializer;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            DataWatcherObject<?> datawatcherobject = (DataWatcherObject<?>) object;

            return this.index == datawatcherobject.index;
        } else {
            return false;
        }
    }
}