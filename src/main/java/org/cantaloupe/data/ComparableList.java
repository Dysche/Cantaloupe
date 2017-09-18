package org.cantaloupe.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to store data in an array-like structure, comparable against
 * other lists.
 * 
 * @author Dylan Scheltens
 *
 * @param <E>
 *            The element type
 */
public class ComparableList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 5088258747540816523L;

    private ComparableList() {
        super();
    }

    private ComparableList(int size) {
        super(size);
    }

    private ComparableList(List<E> other) {
        super(other);
    }

    /**
     * Creates and returns a new comparable list.
     * 
     * @param <E>
     *            The element type
     * @return The list
     */
    public static <E> ComparableList<E> of() {
        return new ComparableList<E>();
    }

    /**
     * Creates and returns a new comparable list with a fixed size.
     * 
     * @param <E>
     *            The element type
     * @param size
     *            The size
     * @return The list
     */
    public static <E> ComparableList<E> of(int size) {
        return new ComparableList<E>(size);
    }

    /**
     * Creates and returns a new comparable list from another list.
     * 
     * @param <E>
     *            The element type
     * @param other
     *            The other list
     * @return The list
     */
    public static <E> ComparableList<E> of(List<E> other) {
        return new ComparableList<E>(other);
    }

    /**
     * Checks if the lists are identical.
     * 
     * @param list
     *            The list
     * @return True if they do, false if not
     */
    public boolean matchesExact(List<E> list) {
        if (this.size() != list.size()) {
            return false;
        }

        for (E e1 : this) {
            for (E e2 : list) {
                if (!e1.equals(e2)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the lists match.
     * 
     * @param list
     *            The list
     * @return True if they do, false if not
     */
    public boolean matches(List<E> list) {
        if (this.size() != list.size()) {
            return false;
        }

        for (E element : list) {
            if (!this.contains(element)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o instanceof ComparableList) {
            return this.matchesExact((ComparableList<E>) o);
        }

        return false;
    }
}