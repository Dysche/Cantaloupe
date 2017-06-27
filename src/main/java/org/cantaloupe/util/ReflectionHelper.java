package org.cantaloupe.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper {
    public static void setField(String fieldName, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = object.getClass().getField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    public static void setDeclaredField(String fieldName, Object object, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    public static Object getStaticField(String fieldName, Class<?> clazz) {
        try {
            Field field = clazz.getField(fieldName);
            field.setAccessible(true);
            
            return field.get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object getDeclaredStaticField(String fieldName, Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            
            return field.get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object invokeMethod(String methodName, Object object, Class<?>[] parameterTypes, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = object.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        
        return method.invoke(object, arguments);
    }

    public static Object invokeMethod(String methodName, Object object, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?>[] parameterTypes = new Class<?>[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            parameterTypes[i] = arguments[i].getClass();
        }

        return invokeMethod(methodName, object, parameterTypes, arguments);
    }

    public static Object invokeDeclaredMethod(String methodName, Object object, Class<?>[] parameterTypes, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = object.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        
        return method.invoke(object, arguments);
    }

    public static Object invokeDeclaredMethod(String methodName, Object object, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?>[] parameterTypes = new Class<?>[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            parameterTypes[i] = arguments[i].getClass();
        }

        return invokeDeclaredMethod(methodName, object, parameterTypes, arguments);
    }
}