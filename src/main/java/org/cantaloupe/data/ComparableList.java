package org.cantaloupe.data;

import java.util.ArrayList;

public class ComparableList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 5088258747540816523L;

    public boolean matchesExact(ComparableList<E> list) {
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

    public boolean matches(ComparableList<E> list) {
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