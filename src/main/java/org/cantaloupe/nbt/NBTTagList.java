package org.cantaloupe.nbt;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;

/**
 * A class used to create a NBT list.
 * 
 * @author Dylan Scheltens
 *
 */
public class NBTTagList {
    private Object handle = null;

    private NBTTagList(Object handle) {
        this.handle = handle;
    }

    public static NBTTagList of(Object handle) {
        return new NBTTagList(handle);
    }

    /**
     * Creates and returns a new NBT list.
     * 
     * @return The NBT list
     */
    public static NBTTagList of() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return new NBTTagList(service.NMS_NBT_TAGLIST_CLASS.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds an object to the NBT list.
     * 
     * @param object The object
     * @return The NBT list
     */
    public NBTTagList add(Object object) {
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
        
        return this;
    }

    /**
     * Gets a compound from the NBT list.
     * 
     * @param index The index
     * @return The compound
     */
    public NBTTagCompound get(int index) {
        try {
            return (NBTTagCompound) ReflectionHelper.invokeMethod("get", this.handle, index);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagCompound.of();
    }

    /**
     * Gets a float from the NBT list.
     * 
     * @param index The index
     * @return The float
     */
    public float getFloat(int index) {
        try {
            return (float) ReflectionHelper.invokeMethod("e", this.handle, index);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0.0F;
    }

    /**
     * Gets a double from the NBT list.
     * 
     * @param index The index
     * @return The double
     */
    public double getDouble(int index) {
        try {
            return (double) ReflectionHelper.invokeMethod("d", this.handle, index);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0.0D;
    }

    /**
     * Gets a string from the NBT list.
     * 
     * @param index The index
     * @return The string
     */
    public String getString(int index) {
        try {
            return (String) ReflectionHelper.invokeMethod("getString", this.handle, index);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Gets the size the NBT list.
     * 
     * @return The size
     */
    public int size() {
        try {
            return (int) ReflectionHelper.invokeMethod("size", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    /**
     * Clones the NBT list.
     * 
     * @return The cloned NBT list
     */
    public NBTTagList clone() {
        try {
            return NBTTagList.of(ReflectionHelper.invokeMethod("clone", this.handle));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagList.of();
    }

    @Override
    public boolean equals(Object object) {
        try {
            return (boolean) ReflectionHelper.invokeMethod("equals", this.handle, object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int hashCode() {
        try {
            return (int) ReflectionHelper.invokeMethod("hashCode", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return super.hashCode();
    }

    @Override
    public String toString() {
        try {
            return (String) ReflectionHelper.invokeMethod("toString", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return super.toString();
    }

    /**
     * Returns a NMS version of the NBT list.
     * 
     * @return The NMS NBT list
     */
    public Object toNMS() {
        return this.handle;
    }
}