package org.cantaloupe.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper {
    public static Object callConstructor(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return clazz.getConstructor().newInstance();
    }

    public static Object callConstructor(Class<?> clazz, Class<?>[] argumentTypes, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return clazz.getConstructor(argumentTypes).newInstance(arguments);
    }

    public static Object callDeclaredConstructor(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        return constructor.newInstance();
    }

    public static Object callDeclaredConstructor(Class<?> clazz, Class<?>[] argumentTypes, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Constructor<?> constructor = clazz.getDeclaredConstructor(argumentTypes);
        constructor.setAccessible(true);

        return constructor.newInstance(arguments);
    }

    public static Object invokeMethod(String methodName, Object object, Class<?>[] argumentTypes, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = object.getClass().getMethod(methodName, argumentTypes);
        method.setAccessible(true);

        return method.invoke(object, arguments);
    }

    public static Object invokeMethod(String methodName, Object object, Class<?> objectClass, Class<?>[] argumentTypes, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = objectClass.getMethod(methodName, argumentTypes);
        method.setAccessible(true);

        return method.invoke(object, arguments);
    }

    public static Object invokeMethod(String methodName, Object object, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?>[] argumentTypes = new Class<?>[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }

        return invokeMethod(methodName, object, argumentTypes, arguments);
    }

    public static Object invokeMethod(String methodName, Object object, Class<?> objectClass, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?>[] argumentTypes = new Class<?>[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }

        return invokeMethod(methodName, object, objectClass, argumentTypes, arguments);
    }

    public static Object invokeDeclaredMethod(String methodName, Object object, Class<?>[] argumentTypes, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = object.getClass().getDeclaredMethod(methodName, argumentTypes);
        method.setAccessible(true);

        return method.invoke(object, arguments);
    }

    public static Object invokeDeclaredMethod(String methodName, Object object, Class<?> objectClass, Class<?>[] argumentTypes, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = objectClass.getDeclaredMethod(methodName, argumentTypes);
        method.setAccessible(true);

        return method.invoke(object, arguments);
    }

    public static Object invokeDeclaredMethod(String methodName, Object object, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?>[] argumentTypes = new Class<?>[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }

        return invokeDeclaredMethod(methodName, object, argumentTypes, arguments);
    }

    public static Object invokeDeclaredMethod(String methodName, Object object, Class<?> objectClass, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?>[] argumentTypes = new Class<?>[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }

        return invokeDeclaredMethod(methodName, object, objectClass, argumentTypes, arguments);
    }

    public static Object invokeStaticMethod(String methodName, Class<?> clazz, Class<?>[] argumentTypes, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = clazz.getMethod(methodName, argumentTypes);
        method.setAccessible(true);

        return method.invoke(null, arguments);
    }

    public static Object invokeStaticMethod(String methodName, Class<?> clazz, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?>[] argumentTypes = new Class<?>[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }

        return invokeStaticMethod(methodName, clazz, argumentTypes, arguments);
    }

    public static void setField(String fieldName, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        setField(fieldName, object.getClass(), object, value);
    }

    public static void setField(String fieldName, Class<?> objectClass, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = objectClass.getField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    public static void setDeclaredField(String fieldName, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        setDeclaredField(fieldName, object.getClass(), object, value);
    }

    public static void setDeclaredField(String fieldName, Class<?> objectClass, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = objectClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    public static void setFieldByType(Class<?> valueClass, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        setFieldByType(valueClass, object.getClass(), object, value);
    }

    public static void setFieldByType(Class<?> valueClass, Class<?> objectClass, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = null;

        for (Field f : objectClass.getFields()) {
            if (f.getType() == valueClass) {
                field = f;
            }
        }

        if (field != null) {
            field.setAccessible(true);
            field.set(object, value);
        }
    }

    public static void setDeclaredFieldByType(Class<?> valueClass, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        setDeclaredFieldByType(valueClass, object.getClass(), object, value);
    }

    public static void setDeclaredFieldByType(Class<?> valueClass, Class<?> objectClass, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = null;

        for (Field f : objectClass.getDeclaredFields()) {
            if (f.getType() == valueClass) {
                field = f;
            }
        }

        if (field != null) {
            field.setAccessible(true);
            field.set(object, value);
        }
    }

    public static Object getField(String fieldName, Object object) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        return getField(fieldName, object.getClass(), object);
    }

    public static Object getField(String fieldName, Class<?> objectClass, Object object) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = objectClass.getField(fieldName);
        field.setAccessible(true);

        return field.get(object);
    }

    public static Object getDeclaredField(String fieldName, Object object) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        return getDeclaredField(fieldName, object.getClass(), object);
    }

    public static Object getDeclaredField(String fieldName, Class<?> objectClass, Object object) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = objectClass.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field.get(object);
    }

    public static Object getStaticField(String fieldName, Class<?> objectClass) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = objectClass.getField(fieldName);
        field.setAccessible(true);

        return field.get(null);
    }

    public static Object getDeclaredStaticField(String fieldName, Class<?> objectClass) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = objectClass.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field.get(null);
    }
}