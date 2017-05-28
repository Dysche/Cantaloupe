package org.cantaloupe.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ComparableList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 5088258747540816523L;

    private UUID              uuid             = null;

    private ComparableList() {
        super();

        this.uuid = UUID.randomUUID();
    }

    private ComparableList(Collection<? extends E> c) {
        super(c);

        this.uuid = UUID.randomUUID();
    }

    private ComparableList(int initialCapacity) {
        super(initialCapacity);

        this.uuid = UUID.randomUUID();
    }

    public static <E> ComparableList<E> of() {
        return new ComparableList<E>();
    }

    public static <E> ComparableList<E> of(Collection<? extends E> c) {
        return new ComparableList<E>(c);
    }

    public static <E> ComparableList<E> of(int initialCapacity) {
        return new ComparableList<E>(initialCapacity);
    }

    @Override
    public boolean add(E e) {
        boolean result = super.add(e);

        if (result) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    public void add(int index, E e) {
        super.add(index, e);

        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = super.addAll(c);

        if (result) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean result = super.addAll(index, c);

        if (result) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    public void clear() {
        super.clear();

        this.uuid = UUID.randomUUID();
    }

    @Override
    public E remove(int index) {
        E result = super.remove(index);

        if (result != null) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);

        if (result) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);

        if (result) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        boolean result = super.removeIf(filter);

        if (result) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);

        this.uuid = UUID.randomUUID();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        super.replaceAll(operator);

        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = super.retainAll(c);

        if (result) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    public E set(int index, E element) {
        E result = super.set(index, element);

        if (result != null) {
            this.uuid = UUID.randomUUID();
        }

        return result;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        super.sort(c);

        this.uuid = UUID.randomUUID();
    }

    @Override
    public void trimToSize() {
        super.trimToSize();

        this.uuid = UUID.randomUUID();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o instanceof ComparableList) {
            return this.uuid.equals(((ComparableList<E>) o).uuid);
        }

        return false;
    }
}