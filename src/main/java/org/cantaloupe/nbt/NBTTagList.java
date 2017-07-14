package org.cantaloupe.nbt;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;

public class NBTTagList {
    private Object handle = null;

    private NBTTagList(Object handle) {
        this.handle = handle;
    }

    public static NBTTagList of(Object handle) {
        return new NBTTagList(handle);
    }

    public static NBTTagList of() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return new NBTTagList(service.NMS_NBT_TAGLIST_CLASS.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void add(Object object) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            if (object instanceof NBTTagCompound) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, ((NBTTagCompound) object).toNMS());
            } else if (object instanceof NBTTagList) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, ((NBTTagList) object).toNMS());
            } else if (object instanceof Byte) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGBYTE_CLASS.getConstructor(byte.class).newInstance(object));
            } else if (object instanceof Short) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGSHORT_CLASS.getConstructor(short.class).newInstance(object));
            } else if (object instanceof Integer) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGINT_CLASS.getConstructor(int.class).newInstance(object));
            } else if (object instanceof Long) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGLONG_CLASS.getConstructor(long.class).newInstance(object));
            } else if (object instanceof Float) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGFLOAT_CLASS.getConstructor(float.class).newInstance(object));
            } else if (object instanceof Double) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGDOUBLE_CLASS.getConstructor(double.class).newInstance(object));
            } else if (object instanceof String) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGSTRING_CLASS.getConstructor(String.class).newInstance(object));
            } else if (object instanceof Byte[]) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGBYTEARRAY_CLASS.getConstructor(byte[].class).newInstance(object));
            } else if (object instanceof Integer[]) {
                ReflectionHelper.invokeMethod("add", this.handle, new Class<?>[] {
                        service.NMS_NBT_BASE_CLASS
                }, service.NMS_NBT_TAGINTARRAY_CLASS.getConstructor(int[].class).newInstance(object));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public NBTTagCompound get(int index) {
        try {
            return (NBTTagCompound) ReflectionHelper.invokeMethod("get", this.handle, index);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagCompound.of();
    }

    public float getFloat(int index) {
        try {
            return (float) ReflectionHelper.invokeMethod("e", this.handle, index);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0.0F;
    }

    public double getDouble(int index) {
        try {
            return (double) ReflectionHelper.invokeMethod("d", this.handle, index);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0.0D;
    }

    public String getString(int index) {
        try {
            return (String) ReflectionHelper.invokeMethod("getString", this.handle, index);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return "";
    }

    public int size() {
        try {
            return (int) ReflectionHelper.invokeMethod("size", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public NBTTagList clone() {
        try {
            return NBTTagList.of(ReflectionHelper.invokeMethod("clone", this.handle));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagList.of();
    }

    public boolean equals(Object object) {
        try {
            return (boolean) ReflectionHelper.invokeMethod("equals", this.handle, object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int hashCode() {
        try {
            return (int) ReflectionHelper.invokeMethod("hashCode", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return super.hashCode();
    }

    public String toString() {
        try {
            return (String) ReflectionHelper.invokeMethod("toString", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return super.toString();
    }

    public Object toNMS() {
        return this.handle;
    }
}