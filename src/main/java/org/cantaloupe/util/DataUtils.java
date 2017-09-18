package org.cantaloupe.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import java.util.WeakHashMap;

import org.cantaloupe.data.ComparableList;
import org.cantaloupe.data.DataContainer;

public class DataUtils {
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    public static <E> ComparableList<E> newComparableList() {
        return ComparableList.<E>of();
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }

    public static <E> Vector<E> newVector() {
        return new Vector<E>();
    }

    public static <E> Stack<E> newStack() {
        return new Stack<E>();
    }

    public static <K, V> DataContainer<K, V> newDataContainer() {
        return DataContainer.<K, V>of();
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> WeakHashMap<K, V> newWeakHashMap() {
        return new WeakHashMap<K, V>();
    }

    public static <K, V> Hashtable<K, V> newHashtable() {
        return new Hashtable<K, V>();
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> clazz) {
        return new EnumMap<K, V>(clazz);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap<K, V>();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    public static <K, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<K, V>();
    }
}