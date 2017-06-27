package org.cantaloupe.data;

public interface Flushable {
    default public void flush() {
        System.out.println(this.toString());
    }
}
