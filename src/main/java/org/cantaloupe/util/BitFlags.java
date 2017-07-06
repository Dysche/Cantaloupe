package org.cantaloupe.util;

public class BitFlags {
    public static boolean isFlagSet(byte flags, byte flag) {
        return (flags & flag) == flag;
    }

    public static byte setFlag(byte flags, byte flag) {
        return (byte) (flags | flag);
    }

    public static byte unsetFlag(byte flags, byte flag) {
        return (byte) (flags & ~flag);
    }
}