package org.cantaloupe.util;

import java.lang.reflect.Field;

public class ReflectionHelper {
    public static void setPrivateField(String fieldName, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}