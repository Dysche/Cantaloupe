package org.cantaloupe.protocol.datawatcher;

public class DataWatcherRegistry {
    public static DataWatcherSerializer<Boolean> BOOLEAN                 = new DataWatcherSerializer<Boolean>('h');
    public static DataWatcherSerializer<Byte>    BYTE                    = new DataWatcherSerializer<Byte>('a');
    public static DataWatcherSerializer<Integer> INTEGER                 = new DataWatcherSerializer<Integer>('b');
    public static DataWatcherSerializer<Float>   FLOAT                   = new DataWatcherSerializer<Float>('c');
    public static DataWatcherSerializer<String>  STRING                  = new DataWatcherSerializer<String>('d');
    public static DataWatcherSerializer<Object>  ICHATBASECOMPONENT      = new DataWatcherSerializer<Object>('e');
    public static DataWatcherSerializer<Object>  ITEMSTACK               = new DataWatcherSerializer<Object>('f');
    public static DataWatcherSerializer<Object>  OPTIONAL_IBLOCKDATA     = new DataWatcherSerializer<Object>('g');
    public static DataWatcherSerializer<Object>  VECTOR3F                = new DataWatcherSerializer<Object>('i');
    public static DataWatcherSerializer<Object>  IBLOCKPOSITION          = new DataWatcherSerializer<Object>('j');
    public static DataWatcherSerializer<Object>  OPTIONAL_IBLOCKPOSITION = new DataWatcherSerializer<Object>('k');
    public static DataWatcherSerializer<Object>  ENUM_DIRECTION          = new DataWatcherSerializer<Object>('l');
    public static DataWatcherSerializer<Object>  OPTIONAL_UUID           = new DataWatcherSerializer<Object>('m');
    public static DataWatcherSerializer<Object>  NBTTAGCOMPOUND          = new DataWatcherSerializer<Object>('n');
}
