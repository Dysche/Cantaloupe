package org.cantaloupe.nbt;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;

/**
 * A class used to create a NBT compound.
 * 
 * @author Dylan Scheltens
 *
 */
public class NBTTagCompound {
    private Object handle = null;

    private NBTTagCompound(Object handle) {
        this.handle = handle;
    }

    /**
     * Creates and returns a new NBT compound.
     * 
     * @return The NBT compound
     */
    public static NBTTagCompound of() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return new NBTTagCompound(service.NMS_NBT_TAGCOMPOUND_CLASS.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates and returns a new NBT compound.
     * 
     * @param handle
     *            The handle
     * 
     * @return The NBT compound
     */
    public static NBTTagCompound of(Object handle) {
        return new NBTTagCompound(handle);
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound set(String key, Object value) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            if (value instanceof NBTTagCompound) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, ((NBTTagCompound) value).toNMS());
            } else if (value instanceof NBTTagList) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, ((NBTTagList) value).toNMS());
            } else if (value instanceof Byte) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGBYTE_CLASS.getConstructor(byte.class).newInstance(value));
            } else if (value instanceof Short) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGSHORT_CLASS.getConstructor(short.class).newInstance(value));
            } else if (value instanceof Integer) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGINT_CLASS.getConstructor(int.class).newInstance(value));
            } else if (value instanceof Long) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGLONG_CLASS.getConstructor(long.class).newInstance(value));
            } else if (value instanceof Float) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGFLOAT_CLASS.getConstructor(float.class).newInstance(value));
            } else if (value instanceof Double) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGDOUBLE_CLASS.getConstructor(double.class).newInstance(value));
            } else if (value instanceof String) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGSTRING_CLASS.getConstructor(String.class).newInstance(value));
            } else if (value instanceof Byte[]) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGBYTEARRAY_CLASS.getConstructor(byte[].class).newInstance(value));
            } else if (value instanceof Integer[]) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, key, service.NMS_NBT_TAGINTARRAY_CLASS.getConstructor(int[].class).newInstance(value));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setByte(String key, byte value) {
        try {
            ReflectionHelper.invokeMethod("setByte", this.handle, new Class<?>[] {
                    String.class, byte.class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setShort(String key, short value) {
        try {
            ReflectionHelper.invokeMethod("setShort", this.handle, new Class<?>[] {
                    String.class, short.class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setInt(String key, int value) {
        try {
            ReflectionHelper.invokeMethod("setInt", this.handle, new Class<?>[] {
                    String.class, int.class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setLong(String key, long value) {
        try {
            ReflectionHelper.invokeMethod("setLong", this.handle, new Class<?>[] {
                    String.class, long.class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setFloat(String key, float value) {
        try {
            ReflectionHelper.invokeMethod("setFloat", this.handle, new Class<?>[] {
                    String.class, float.class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setDouble(String key, double value) {
        try {
            ReflectionHelper.invokeMethod("setDouble", this.handle, new Class<?>[] {
                    String.class, double.class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setString(String key, String value) {
        try {
            ReflectionHelper.invokeMethod("setString", this.handle, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setByteArray(String key, byte[] value) {
        try {
            ReflectionHelper.invokeMethod("setByteArray", this.handle, new Class<?>[] {
                    String.class, byte[].class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setIntArray(String key, int[] value) {
        try {
            ReflectionHelper.invokeMethod("setIntArray", this.handle, new Class<?>[] {
                    String.class, int[].class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets an entry in the compound.
     * 
     * @param key
     *            The key of the entry
     * @param value
     *            The value of the entry
     * @return The compound
     */
    public NBTTagCompound setBoolean(String key, boolean value) {
        try {
            ReflectionHelper.invokeMethod("setBoolean", this.handle, new Class<?>[] {
                    String.class, boolean.class
            }, key, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Checks if the compound has an entry.
     * 
     * @param key
     *            The key of the entry
     * @return True if it does, false if not
     */
    public boolean hasKey(String key) {
        try {
            return (boolean) ReflectionHelper.invokeMethod("hasKey", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks if the compound has an entry.
     * 
     * @param key
     *            The key of the entry
     * @param i
     *            The type
     * 
     * @return True if it does, false if not
     */
    public boolean hasKeyOfType(String key, int i) {
        try {
            return (boolean) ReflectionHelper.invokeMethod("hasKeyOfType", this.handle, new Class<?>[] {
                    int.class
            }, key, i);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets an object from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The object
     */
    public Object get(String key) {
        try {
            return ReflectionHelper.invokeMethod("get", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets a byte from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The byte
     */
    public byte getByte(String key) {
        try {
            return (byte) ReflectionHelper.invokeMethod("getByte", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return (byte) 0;
    }

    /**
     * Gets a short from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The short
     */
    public short getShort(String key) {
        try {
            return (short) ReflectionHelper.invokeMethod("getShort", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return (short) 0;
    }

    /**
     * Gets an int from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The int
     */
    public int getInt(String key) {
        try {
            return (int) ReflectionHelper.invokeMethod("getInt", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Gets a long from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The long
     */
    public long getLong(String key) {
        try {
            return (long) ReflectionHelper.invokeMethod("getLong", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    /**
     * Gets a float from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The float
     */
    public float getFloat(String key) {
        try {
            return (float) ReflectionHelper.invokeMethod("getFloat", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0.0F;
    }

    /**
     * Gets a double from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The double
     */
    public double getDouble(String key) {
        try {
            return (double) ReflectionHelper.invokeMethod("getDouble", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0.0D;
    }

    /**
     * Gets a string from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The string
     */
    public String getString(String key) {
        try {
            return (String) ReflectionHelper.invokeMethod("getString", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Gets a byte array from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The byte array
     */
    public byte[] getByteArray(String key) {
        try {
            return (byte[]) ReflectionHelper.invokeMethod("getByteArray", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    /**
     * Gets an int array from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The int array
     */
    public int[] getIntArray(String key) {
        try {
            return (int[]) ReflectionHelper.invokeMethod("getIntArray", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return new int[0];
    }

    /**
     * Gets a compound from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The compound
     */
    public NBTTagCompound getCompound(String key) {
        try {
            return NBTTagCompound.of(ReflectionHelper.invokeMethod("getCompound", this.handle, key));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagCompound.of();
    }

    /**
     * Gets a list from the compound.
     * 
     * @param key
     *            The key of the value
     * @param index
     *            The index of the list
     * 
     * @return The list
     */
    public NBTTagList getList(String key, int index) {
        try {
            return NBTTagList.of(ReflectionHelper.invokeMethod("getList", this.handle, key, index));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagList.of();
    }

    /**
     * Gets a boolean from the compound.
     * 
     * @param key
     *            The key of the value
     * @return The boolean
     */
    public boolean getBoolean(String key) {
        try {
            return (boolean) ReflectionHelper.invokeMethod("getBoolean", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Removes a value from the compound.
     * 
     * @param key
     *            The key of the value
     */
    public void remove(String key) {
        try {
            ReflectionHelper.invokeMethod("remove", this.handle, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the NBT compound is empty.
     * 
     * @return True if it is, false if not
     */
    public boolean isEmpty() {
        try {
            return (boolean) ReflectionHelper.invokeMethod("isEmpty", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Clones the NBT compound.
     * 
     * @return The cloned NBT compound
     */
    public NBTTagCompound clone() {
        try {
            return (NBTTagCompound) ReflectionHelper.invokeMethod("clone", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagCompound.of();
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
     * Returns a NMS version of the NBT compound.
     * 
     * @return The NMS NBT compound
     */
    public Object toNMS() {
        return this.handle;
    }
}