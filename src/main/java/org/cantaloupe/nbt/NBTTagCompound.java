package org.cantaloupe.nbt;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.util.ReflectionHelper;

public class NBTTagCompound {
    private Object handle = null;

    private NBTTagCompound(Object handle) {
        this.handle = handle;
    }

    public static NBTTagCompound of(Object handle) {
        return new NBTTagCompound(handle);
    }

    public static NBTTagCompound of() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            return new NBTTagCompound(service.NMS_NBT_TAGCOMPOUND_CLASS.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void set(String s, Object object) {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            if (object instanceof NBTTagCompound) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, ((NBTTagCompound) object).toNMS());
            } else if (object instanceof NBTTagList) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, ((NBTTagList) object).toNMS());
            } else if (object instanceof Byte) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGBYTE_CLASS.getConstructor(byte.class).newInstance(object));
            } else if (object instanceof Short) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGSHORT_CLASS.getConstructor(short.class).newInstance(object));
            } else if (object instanceof Integer) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGINT_CLASS.getConstructor(int.class).newInstance(object));
            } else if (object instanceof Long) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGLONG_CLASS.getConstructor(long.class).newInstance(object));
            } else if (object instanceof Float) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGFLOAT_CLASS.getConstructor(float.class).newInstance(object));
            } else if (object instanceof Double) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGDOUBLE_CLASS.getConstructor(double.class).newInstance(object));
            } else if (object instanceof String) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGSTRING_CLASS.getConstructor(String.class).newInstance(object));
            } else if (object instanceof Byte[]) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGBYTEARRAY_CLASS.getConstructor(byte[].class).newInstance(object));
            } else if (object instanceof Integer[]) {
                ReflectionHelper.invokeMethod("set", this.handle, new Class<?>[] {
                        String.class, service.NMS_NBT_BASE_CLASS
                }, s, service.NMS_NBT_TAGINTARRAY_CLASS.getConstructor(int[].class).newInstance(object));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void setByte(String s, byte b0) {
        try {
            ReflectionHelper.invokeMethod("setByte", this.handle, s, b0);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setShort(String s, short short1) {
        try {
            ReflectionHelper.invokeMethod("setShort", this.handle, s, short1);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setInt(String s, int i) {
        try {
            ReflectionHelper.invokeMethod("setInt", this.handle, s, i);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setLong(String s, long i) {
        try {
            ReflectionHelper.invokeMethod("setLong", this.handle, s, i);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setFloat(String s, float f) {
        try {
            ReflectionHelper.invokeMethod("setFloat", this.handle, s, f);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setDouble(String s, double d0) {
        try {
            ReflectionHelper.invokeMethod("setDouble", this.handle, s, d0);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setString(String s, String s1) {
        try {
            ReflectionHelper.invokeMethod("setString", this.handle, s, s1);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setByteArray(String s, byte[] abyte) {
        try {
            ReflectionHelper.invokeMethod("setByteArray", this.handle, s, abyte);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setIntArray(String s, int[] aint) {
        try {
            ReflectionHelper.invokeMethod("setIntArray", this.handle, s, aint);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setBoolean(String s, boolean flag) {
        this.setByte(s, (byte) (flag ? 1 : 0));
    }

    public Object get(String s) {
        try {
            return ReflectionHelper.invokeMethod("get", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean hasKey(String s) {
        try {
            return (boolean) ReflectionHelper.invokeMethod("hasKey", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hasKeyOfType(String s, int i) {
        try {
            return (boolean) ReflectionHelper.invokeMethod("hasKeyOfType", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    public byte getByte(String s) {
        try {
            return (byte) ReflectionHelper.invokeMethod("getByte", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return (byte) 0;
    }

    public short getShort(String s) {
        try {
            return (short) ReflectionHelper.invokeMethod("getShort", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return (short) 0;
    }

    public int getInt(String s) {
        try {
            return (int) ReflectionHelper.invokeMethod("getInt", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long getLong(String s) {
        try {
            return (long) ReflectionHelper.invokeMethod("getLong", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public float getFloat(String s) {
        try {
            return (float) ReflectionHelper.invokeMethod("getFloat", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0.0F;
    }

    public double getDouble(String s) {
        try {
            return (double) ReflectionHelper.invokeMethod("getDouble", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return 0.0D;
    }

    public String getString(String s) {
        try {
            return (String) ReflectionHelper.invokeMethod("getString", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return "";
    }

    public byte[] getByteArray(String s) {
        try {
            return (byte[]) ReflectionHelper.invokeMethod("getByteArray", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    public int[] getIntArray(String s) {
        try {
            return (int[]) ReflectionHelper.invokeMethod("getIntArray", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return new int[0];
    }

    public NBTTagCompound getCompound(String s) {
        try {
            return NBTTagCompound.of(ReflectionHelper.invokeMethod("getCompound", this.handle, s));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagCompound.of();
    }

    public NBTTagList getList(String s, int i) {
        try {
            return NBTTagList.of(ReflectionHelper.invokeMethod("getList", this.handle, s, i));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagList.of();
    }

    public boolean getBoolean(String s) {
        return this.getByte(s) != 0;
    }

    public void remove(String s) {
        try {
            ReflectionHelper.invokeMethod("remove", this.handle, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        try {
            return (boolean) ReflectionHelper.invokeMethod("isEmpty", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return true;
    }

    public NBTTagCompound clone() {
        try {
            return (NBTTagCompound) ReflectionHelper.invokeMethod("clone", this.handle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return NBTTagCompound.of();
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